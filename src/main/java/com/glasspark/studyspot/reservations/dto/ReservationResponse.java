package com.glasspark.studyspot.reservations.dto;

import com.glasspark.studyspot.reservations.domain.ReservationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 내 예약 목록의 한 행.
 * 예약(reservations)에 룸 이름(study_rooms.name)을 조인한 조회 결과를 MyBatis 가 필드에 바로 채운다.
 * (그래서 Reservation 도메인과 별도로 둔다. 도메인에는 없는 studyRoomName 이 필요하기 때문)
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // MyBatis 만 생성한다
public class ReservationResponse {

    private Long reservationId;
    private Long studyRoomId;
    private String studyRoomName;
    private String reserverName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private ReservationStatus status;          // JSON: "CONFIRMED"  (로직용)

    // JSON: "예약 확정" (화면 표시용). status 로부터 계산하므로 필드는 따로 없다.
    public String getStatusDescription() {
        return status == null ? null : status.getDescription();
    }
}
