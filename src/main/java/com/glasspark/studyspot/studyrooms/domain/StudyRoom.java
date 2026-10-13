package com.glasspark.studyspot.studyrooms.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 함부로 생성하지 못하도록 new StudyRoom();
public class StudyRoom {

    private Long studyRoomId;
    private String name;
    private String address;
    private Integer capacity;
    private LocalTime openTime;
    private LocalTime closeTime;
    private StudyRoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(access = AccessLevel.PROTECTED) // 외부에서 함부로 Builder를 사용하지 못하도록 StudyRoom.builder() 제한
    private StudyRoom(Long studyRoomId, String name, String address, Integer capacity,
                      LocalTime openTime, LocalTime closeTime, StudyRoomStatus status,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.studyRoomId = studyRoomId;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static StudyRoom create(String name, String address, Integer capacity,
                                   LocalTime openTime, LocalTime closeTime) {
        return StudyRoom.builder()
                .name(name)
                .address(address)
                .capacity(capacity)
                .openTime(openTime)
                .closeTime(closeTime)
                .status(StudyRoomStatus.ACTIVE)
                .build();
    }

    // open_time / close_time 을 운영 시간 규칙(야간, 24시간 등)으로 해석한 값. DB 컬럼이 아니다.
    public OperatingHours operatingHours() {
        return new OperatingHours(openTime, closeTime);
    }
}
