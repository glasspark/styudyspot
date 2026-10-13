package com.glasspark.studyspot.studyrooms.dto;

import com.glasspark.studyspot.studyrooms.domain.StudyRoom;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서는 from() 으로만 생성
public class StudyRoomResponse {

    private final Long studyRoomId;
    private final String name;
    private final String address;
    private final Integer capacity;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final String status;              // "ACTIVE"  (로직용)
    private final String statusDescription;   // "운영 중"  (화면 표시용)

    public static StudyRoomResponse from(StudyRoom studyRoom) {
        return new StudyRoomResponse(
                studyRoom.getStudyRoomId(),
                studyRoom.getName(),
                studyRoom.getAddress(),
                studyRoom.getCapacity(),
                studyRoom.getOpenTime(),
                studyRoom.getCloseTime(),
                studyRoom.getStatus().name(),
                studyRoom.getStatus().getDescription()
        );
    }
}
