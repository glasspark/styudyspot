package com.glasspark.studyspot.studyrooms.dto;

import com.glasspark.studyspot.studyrooms.domain.OperatingHours;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StudyRoomRegisterRequest {

    @NotBlank(message = "스터디룸 이름은 필수입니다.")
    @Size(max = 100, message = "스터디룸 이름은 100자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 255, message = "주소는 255자 이하로 입력해주세요.")
    private String address;

    @NotNull(message = "수용 인원은 필수입니다.")
    @Min(value = 1, message = "수용 인원은 1명 이상이어야 합니다.")
    @Max(value = 100, message = "수용 인원은 100명 이하로 입력해주세요.")
    private Integer capacity;

    @NotNull(message = "운영 시작 시간은 필수입니다.")
    private LocalTime openTime;

    @NotNull(message = "운영 종료 시간은 필수입니다.")
    private LocalTime closeTime;

    // 운영 시간 해석 규칙은 OperatingHours 참고
    //  - 시작 < 종료  : 같은 날 운영 (예: 09:00~22:00)
    //  - 시작 > 종료  : 다음 날로 넘어가는 야간 운영 (예: 18:00~02:00, 자정 마감 09:00~00:00)
    //  - 시작 = 종료  : 24시간 운영
    // 24시간 운영이 아니면 운영 구간이 최소 1시간 이상이어야 한다. (null 은 위의 @NotNull 이 처리)
    // @AssertTrue : 해당 값이 반드시 true여야 한다는 검증 조건을 지정하는 어노테이션. 직접 호출은 없어도
    // @Valid가 @AssertTrue 처럼 제약 어노테이션을 보고서 실행시킨다.
    @AssertTrue(message = "운영 시간이 올바르지 않습니다. 24시간 운영이 아니라면 운영 구간이 1시간 이상이어야 합니다. (24시간 운영은 시작과 종료를 같게 입력)")
    public boolean isValidOperatingHours() {
        if (openTime == null || closeTime == null) {
            return true;
        }
        OperatingHours hours = new OperatingHours(openTime, closeTime);
        // 24시간 운영이거나 최소 운영시간 이상인지 확인
        return hours.is24Hours() || hours.windowLength().compareTo(OperatingHours.MIN_WINDOW) >= 0;
    }

}
