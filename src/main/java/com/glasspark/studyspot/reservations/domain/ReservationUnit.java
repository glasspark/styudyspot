package com.glasspark.studyspot.reservations.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 예약 시간 단위 규칙.
 *
 * <pre>
 *  길이 : 30분 또는 1시간 둘 중 하나
 *  시작 : 정각 또는 30분에만 가능  예) 10:00, 10:30 (O)  10:15 (X)
 * </pre>
 *
 * 룸의 운영 시간 안에 들어가는지는 따로 OperatingHours 가 판단한다. 이 규칙은 DB 를 쓰지 않으므로 룸 락 앞에서 검사해도 된다.
 */
public final class ReservationUnit {

    /** 예약이 시작할 수 있는 간격(분). 60 의 약수이므로 정각과 30분이 된다. */
    public static final int START_STEP_MINUTES = 30;

    /** 허용하는 예약 길이 */
    private static final Set<Duration> ALLOWED_LENGTHS = Set.of(Duration.ofMinutes(30), Duration.ofHours(1));

    private ReservationUnit() {
    }

    /** [start, end) 가 정해진 단위(시작 정각·30분, 길이 30분·1시간)를 지키는지. */
    public static boolean isValid(LocalDateTime start, LocalDateTime end) {
        return startsOnStep(start) && ALLOWED_LENGTHS.contains(Duration.between(start, end));
    }

    // 시작이 30분 간격에 맞는지 (초, 나노초가 남아 있으면 10:00:30 같은 값이 통과하므로 함께 확인)
    private static boolean startsOnStep(LocalDateTime start) {
        return start.getMinute() % START_STEP_MINUTES == 0 && start.getSecond() == 0 && start.getNano() == 0;
    }
}
