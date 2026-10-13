package com.glasspark.studyspot.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {

    @Min(value = 0, message = "page는 0 이상이어야 합니다.")
    private int page = 0;

    @Min(value = 1, message = "size는 1 이상이어야 합니다.")
    @Max(value = 50, message = "size는 50 이하로 입력해주세요.")
    private int size = 10;

    // int 곱셈 오버플로를 피하려고 long으로 계산. MyBatis의 #{offset}이 이 getter를 읽는다.
    public long getOffset() {
        return (long) page * size;
    }
}
