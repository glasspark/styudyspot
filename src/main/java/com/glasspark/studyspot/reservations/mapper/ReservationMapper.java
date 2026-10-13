package com.glasspark.studyspot.reservations.mapper;

import com.glasspark.studyspot.reservations.domain.Reservation;
import com.glasspark.studyspot.reservations.dto.ReservationResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReservationMapper {

    void insert(Reservation reservation);

    int countOverlap(@Param("studyRoomId") Long studyRoomId, @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

    int cancel(@Param("reservationId") Long reservationId, @Param("memberId") Long memberId);

    // 내 예약 목록 (룸 이름 포함)
    List<ReservationResponse> findByMemberId(@Param("memberId") Long memberId, @Param("offset") long offset, @Param("size") int size);

    long countByMemberId(@Param("memberId") Long memberId);

}
