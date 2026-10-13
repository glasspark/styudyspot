package com.glasspark.studyspot.members.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //외부에서 함부로 생성하지 못하도록 new Member();
public class Member {

    private Long memberId;
    private String username;
    private String email;
    private String password;
    private String nickname;
    private String role;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(access = AccessLevel.PROTECTED) /// 외부에서 함부로 Builder를 사용하지 못하도록 Member.builder() 제한
    private Member(Long memberId, String username, String email, String password, String nickname,
                   String role, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.memberId = memberId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Member createMember(String username, String email, String encodedPassword, String nickname) {
        return Member.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .role("USER")
                .status("ACTIVE")
                .build();
    }
}
