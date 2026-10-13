package com.glasspark.studyspot.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    DUPLICATE_MEMBER_INFO(HttpStatus.CONFLICT, "이미 사용 중인 정보입니다. 다시 시도해주세요."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    DUPLICATE_STUDY_ROOM(HttpStatus.CONFLICT, "이미 등록된 스터디룸입니다"),
    STUDY_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 스터디룸입니다."),
    STUDY_ROOM_NOT_AVAILABLE(HttpStatus.CONFLICT, "현재 예약할 수 없는 스터디룸입니다."),
    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "예약 시간이 올바르지 않습니다."),
    RESERVATION_TIME_CONFLICT(HttpStatus.CONFLICT, "이미 예약된 시간대입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
