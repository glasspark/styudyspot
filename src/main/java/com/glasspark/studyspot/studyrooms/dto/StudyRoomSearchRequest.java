package com.glasspark.studyspot.studyrooms.dto;

import com.glasspark.studyspot.common.PageRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyRoomSearchRequest extends PageRequest {

    @Size(max = 50, message = "검색어는 50자 이하로 입력해주세요.")
    private String keyword;          // 이름 또는 주소에 포함된 글자

    @Min(value = 1, message = "최소 수용 인원은 1 이상이어야 합니다.")
    private Integer minCapacity;     // 이 인원 이상 수용 가능한 룸

}
