package com.glasspark.studyspot.reservations.controller;


import com.glasspark.studyspot.common.ApiResponse;
import com.glasspark.studyspot.common.PageRequest;
import com.glasspark.studyspot.common.PageResponse;
import com.glasspark.studyspot.config.security.CustomUserDetails;
import com.glasspark.studyspot.reservations.dto.ReservationCreateRequest;
import com.glasspark.studyspot.reservations.dto.ReservationResponse;
import com.glasspark.studyspot.reservations.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationApiController {

    private ReservationService reservationService;

    @Operation(summary = "스터디룸 예약", description = "스터디룸과 시간대를 받아 로그인한 회원의 예약을 확정")
    @PostMapping
    public ApiResponse<Void> reserve(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @Valid @RequestBody ReservationCreateRequest request) {
        return reservationService.reserve(userDetails.getMemberId(), request);
    }

    @Operation(summary = "내 예약 목록", description = "로그인한 회원의 예약을 최신 예약 시간순으로 페이징 조회")
    @GetMapping("/me")
    public ApiResponse<PageResponse<ReservationResponse>> getMyReservations(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid PageRequest pageRequest) {
        return reservationService.getMyReservations(userDetails.getMemberId(), pageRequest);
    }

    @Operation(summary = "예약 취소", description = "로그인한 회원 본인의 예약을 취소 처리")
    @PatchMapping("/{reservationId}/cancel")
    public ApiResponse<Void> cancel(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long reservationId) {
        return reservationService.cancel(userDetails.getMemberId(), reservationId);
    }

}
