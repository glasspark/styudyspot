package com.glasspark.studyspot.reservations.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 함부로 생성하지 못하도록 new Reservation();
public class Reservation {

    private Long reservationId;
    private Long studyRoomId;
    private Long memberId;
    private String reserverName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(access = AccessLevel.PROTECTED) // 외부에서 함부로 Builder를 사용하지 못하도록 Reservation.builder() 제한
    private Reservation(Long reservationId, Long studyRoomId, Long memberId, String reserverName,
                        LocalDateTime startAt, LocalDateTime endAt, ReservationStatus status,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.reservationId = reservationId;
        this.studyRoomId = studyRoomId;
        this.memberId = memberId;
        this.reserverName = reserverName;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Reservation create(Long studyRoomId, Long memberId, String reserverName,
                                     LocalDateTime startAt, LocalDateTime endAt) {
        return Reservation.builder()
                .studyRoomId(studyRoomId)
                .memberId(memberId)
                .reserverName(reserverName)
                .startAt(startAt)
                .endAt(endAt)
                .status(ReservationStatus.CONFIRMED)
                .build();
    }
}
