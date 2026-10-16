package com.glasspark.studyspot.reservations.service;

import com.glasspark.studyspot.common.*;
import com.glasspark.studyspot.reservations.domain.Reservation;
import com.glasspark.studyspot.reservations.domain.ReservationUnit;
import com.glasspark.studyspot.reservations.dto.ReservationCreateRequest;
import com.glasspark.studyspot.reservations.dto.ReservationResponse;
import com.glasspark.studyspot.reservations.mapper.ReservationMapper;
import com.glasspark.studyspot.studyrooms.domain.StudyRoom;
import com.glasspark.studyspot.studyrooms.domain.StudyRoomStatus;
import com.glasspark.studyspot.studyrooms.mapper.StudyRoomMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final StudyRoomMapper studyRoomMapper;

    @Transactional
    public ApiResponse<Void> reserve(Long memberId, ReservationCreateRequest request) {

        // 예약 가능한지 확인(request 로 넘어온 시간에 대해서 시작 > 종료 와 같이 유효성 검사
        validateBasicTime(request.getStartAt(), request.getEndAt());

        // 정해진 단위(30분/1시간, 시작은 정각·30분)인지 확인. DB 를 쓰지 않는 검증이라 락 앞에 두어도 안전하다.
        validateReservationUnit(request.getStartAt(), request.getEndAt());

        // FOR UPDATE 로 첫 락
        // 룸 존재 / ACTIVE 여부 / 시간 규칙
        StudyRoom room = studyRoomMapper.lockById(request.getStudyRoomId());
        if (room == null) throw new CustomException(ErrorCode.STUDY_ROOM_NOT_FOUND);
        if (room.getStatus() != StudyRoomStatus.ACTIVE) throw new CustomException(ErrorCode.STUDY_ROOM_NOT_AVAILABLE);
        validateOperatingHours(room, request.getStartAt(), request.getEndAt());

        // 겹침 검사
        if (reservationMapper.countOverlap(room.getStudyRoomId(), request.getStartAt(), request.getEndAt()) > 0) {
            throw new CustomException(ErrorCode.RESERVATION_TIME_CONFLICT);
        }

        // 저장
        // memberId 는 로그인 정보에서, reserverName 은 요청 본문(앞뒤 공백 제거)에서 온다.
        reservationMapper.insert(Reservation.create(room.getStudyRoomId(), memberId, request.getReserverName().trim(),
                request.getStartAt(), request.getEndAt()));
        return ApiResponse.ok();
    }

    // request 로 넘어온 시간의 유효성 검사 (DB 없이 가능한 검증). 야간 운영을 지원하므로 날짜가 달라도 되며 대신 끝나는 날짜는 시작 날짜를 넘을 수 없다.
    private void validateBasicTime(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {                                         // 시작 < 종료인지 확인
            throw new CustomException(ErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    // 예약 시간 단위 확인. 시작은 정각·30분, 길이는 30분·1시간 (규칙은 ReservationUnit)
    private void validateReservationUnit(LocalDateTime start, LocalDateTime end) {
        if (!ReservationUnit.isValid(start, end)) {
            throw new CustomException(ErrorCode.INVALID_RESERVATION_UNIT);
        }
    }

    // 해당 스터디룸의 운영 시간 확인
    // 야간 운영(날짜를 넘김), 24시간 운영 해석 규칙은 OperatingHours
    private void validateOperatingHours(StudyRoom room, LocalDateTime start, LocalDateTime end) {
        if (!room.operatingHours().canAccommodate(start, end)) {
            throw new CustomException(ErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    //예약 취소
    public ApiResponse<Void> cancel(Long memberId, Long reservationId) {
        if (reservationMapper.cancel(reservationId, memberId) == 0) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        return ApiResponse.ok();
    }

    // 내 예약 목록
    public ApiResponse<PageResponse<ReservationResponse>> getMyReservations(Long memberId, PageRequest pageRequest) {
        List<ReservationResponse> list =
                reservationMapper.findByMemberId(memberId, pageRequest.getOffset(), pageRequest.getSize());
        long totalElements = reservationMapper.countByMemberId(memberId);
        return ApiResponse.ok(PageResponse.of(list, pageRequest.getPage(), pageRequest.getSize(), totalElements));
    }

}
