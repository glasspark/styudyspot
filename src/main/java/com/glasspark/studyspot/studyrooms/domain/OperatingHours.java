package com.glasspark.studyspot.studyrooms.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * 스터디룸의 운영 시간. DB 에는 open_time / close_time 두 개의 TIME 으로만 저장하고, 아래 규칙으로 해석한다.
 *
 * <pre>
 *  open 이 close 보다 이름 : 같은 날 운영          예) 09:00~22:00  → D 09:00 ~ D 22:00
 *  open 이 close 보다 늦음 : 야간(날짜를 넘김) 운영   예) 18:00~02:00  → D 18:00 ~ D+1 02:00
 *                          (자정 마감 09:00~00:00 도 여기에 포함 → D 09:00 ~ D+1 00:00)
 *  open 과 close 가 같음   : 24시간 운영
 * </pre>
 *
 * 예약은 하나의 운영 구간 안에 통째로 들어가야 한다. (영업 종료 후 재오픈 사이를 걸치는 예약은 불가)
 * 단, 24시간 운영은 구간 제한이 없는 대신 한 건당 최대 24시간까지만 예약할 수 있다.
 */
public final class OperatingHours {

    /** 운영 구간(24시간 운영 제외)의 최소 시간 설정  */
    public static final Duration MIN_WINDOW = Duration.ofHours(1);

    /** 24시간 운영 룸의 예약 한 건당 최대 시간 */
    public static final Duration MAX_RESERVATION_FOR_24_HOURS = Duration.ofHours(24);

    /* 하루가 24시간임을 나타내는 상수 */
    private static final Duration ONE_DAY = Duration.ofHours(24);

    private final LocalTime open;
    private final LocalTime close;

    public OperatingHours(LocalTime open, LocalTime close) {
        this.open = Objects.requireNonNull(open, "open");
        this.close = Objects.requireNonNull(close, "close");
    }

    // 24시간 영업인지 확인. 24시간이면 true를 반환
    public boolean is24Hours() {
        return open.equals(close);
    }

    // 닫는 시각이 여는 시각보다 이르면 다음 날로 넘어가는 야간 운영 (자정 마감 포함)
    public boolean isOvernight() {
        return close.isBefore(open);
    }

    /** 하루 운영 구간의 길이. 24시간 운영이면 24시간. */
    public Duration windowLength() {
        if (is24Hours()) {
            return ONE_DAY;
        }
        Duration length = Duration.between(open, close);     // 야간이면 음수 (예: 18:00 → 02:00 = -16h)
        return length.isNegative() ? length.plus(ONE_DAY) : length;
    }

    /** [start, end) 예약이 이 운영 시간 안에 통째로 들어가는지. */
    public boolean canAccommodate(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            return false;
        }
        if (is24Hours()) {
            return Duration.between(start, end).compareTo(MAX_RESERVATION_FOR_24_HOURS) <= 0;
        }

        // 시작 시각을 품을 수 있는 운영 구간은 '시작일 구간'과 '전날에 시작한 야간 구간' 둘뿐이다.
        LocalDate startDate = start.toLocalDate();
        for (LocalDate businessDay : new LocalDate[]{startDate, startDate.minusDays(1)}) {
            LocalDateTime windowStart = businessDay.atTime(open);
            LocalDateTime windowEnd = windowStart.plus(windowLength());
            if (!start.isBefore(windowStart) && !end.isAfter(windowEnd)) {
                return true;
            }
        }
        return false;
    }
}
