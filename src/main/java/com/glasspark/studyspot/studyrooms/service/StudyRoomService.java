package com.glasspark.studyspot.studyrooms.service;

import com.glasspark.studyspot.common.*;
import com.glasspark.studyspot.studyrooms.domain.StudyRoom;
import com.glasspark.studyspot.studyrooms.domain.StudyRoomStatus;
import com.glasspark.studyspot.studyrooms.dto.StudyRoomRegisterRequest;
import com.glasspark.studyspot.studyrooms.dto.StudyRoomResponse;
import com.glasspark.studyspot.studyrooms.dto.StudyRoomSearchRequest;
import com.glasspark.studyspot.studyrooms.mapper.StudyRoomMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudyRoomService {

    private final StudyRoomMapper studyRoomMapper;

    public ApiResponse<Void> createStudyRoom(StudyRoomRegisterRequest request) {

        // 같은 주소에 다른 이름의 룸은 등록할 수 있다. (같은 건물에 룸이 여러 개)
        if (studyRoomMapper.countByAddressAndName(request.getAddress(), request.getName()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_STUDY_ROOM);
        }

        StudyRoom studyRoom = StudyRoom.create(request.getName(),
                request.getAddress(),
                request.getCapacity(),
                request.getOpenTime(),
                request.getCloseTime());
        try {
            studyRoomMapper.insertStudyRoom(studyRoom);
        } catch (DuplicateKeyException e) {
            throw new CustomException(ErrorCode.DUPLICATE_STUDY_ROOM);
        }
        return ApiResponse.ok();
    }

    // 룸 상세 조회. 폐쇄된 룸은 없는 룸으로 취급한다. (임시 중지 룸은 보여주되 예약만 막는다)
    public ApiResponse<StudyRoomResponse> getStudyRoom(Long studyRoomId) {
        StudyRoom studyRoom = studyRoomMapper.findById(studyRoomId);
        if (studyRoom == null || studyRoom.getStatus() == StudyRoomStatus.CLOSED) {
            throw new CustomException(ErrorCode.STUDY_ROOM_NOT_FOUND);
        }
        return ApiResponse.ok(StudyRoomResponse.from(studyRoom));
    }

    public ApiResponse<PageResponse<StudyRoomResponse>> getStudyRoomList(StudyRoomSearchRequest request) {
        List<StudyRoomResponse> list = studyRoomMapper.findAll(request).stream()
                .map(StudyRoomResponse::from).toList();
        // count 에도 같은 request 를 넘겨야 필터가 적용된 전체 건수가 나온다.
        return ApiResponse.ok(PageResponse.of(list, request.getPage(), request.getSize(),
                studyRoomMapper.count(request)));
    }

}
