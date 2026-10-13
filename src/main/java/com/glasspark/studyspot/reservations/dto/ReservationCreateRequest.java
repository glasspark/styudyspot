package com.glasspark.studyspot.reservations.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationCreateRequest {

    @NotNull(message = "스터디룸은 필수입니다.")
    private Long studyRoomId;

    // 업체가 예약 목록에서 보는 표시용 이름. 신원 확인에는 쓰지 않는다. (신원은 member_id)
    @NotBlank(message = "예약자 이름은 필수입니다.")
    @Size(max = 50, message = "예약자 이름은 50자 이하로 입력해주세요.")
    private String reserverName;

    @NotNull(message = "예약 시작 시간은 필수입니다.")
    @Future(message = "예약 시작 시간은 현재 이후여야 합니다.")
    private LocalDateTime startAt;

    @NotNull(message = "예약 종료 시간은 필수입니다.")
    private LocalDateTime endAt;

    // memberId 는 일부러 받지 않는다. 요청 본문으로 받으면 남의 이름으로 예약할 수 있으므로
    // 서비스 호출 시 로그인 정보(CustomUserDetails.getMemberId())에서 꺼내 넘긴다.
    // (예약자 이름 reserverName 은 표시용 입력값이라 본문으로 받는다)
}
