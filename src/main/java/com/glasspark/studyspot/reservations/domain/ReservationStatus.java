package com.glasspark.studyspot.reservations.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    CONFIRMED("예약 확정"),
    CANCELLED("예약 취소");

    private final String description;
}
