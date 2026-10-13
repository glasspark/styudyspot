package com.glasspark.studyspot.members.controller;

import com.glasspark.studyspot.common.ApiResponse;
import com.glasspark.studyspot.config.security.CustomUserDetails;
import com.glasspark.studyspot.members.domain.Member;
import com.glasspark.studyspot.members.dto.MemberJoinRequest;
import com.glasspark.studyspot.members.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/members")
@AllArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원 정보를 입력받아 신규 회원을 등록")
    @PostMapping
    public ApiResponse<Void> join(@Valid @RequestBody MemberJoinRequest request) {
        return memberService.join(request);
    }


    @Operation(summary = "회원탈퇴", description = "회원가입된 회원을 탈퇴")
    @DeleteMapping
    public ApiResponse<Void> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    HttpServletRequest request) throws ServletException {
        ApiResponse<Void> result = memberService.delete(userDetails.getMemberId());
        request.logout(); // securityConfig 에 있는 logout 실행
        return result;
    }
}
