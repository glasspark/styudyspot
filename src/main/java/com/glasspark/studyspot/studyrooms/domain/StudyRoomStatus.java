package com.glasspark.studyspot.studyrooms.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyRoomStatus {
    ACTIVE("운영 중"),
    SUSPENDED("임시 중지"),
    CLOSED("폐쇄");

    private final String description;
}