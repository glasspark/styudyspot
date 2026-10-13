package com.glasspark.studyspot.members.service;

import com.glasspark.studyspot.common.ApiResponse;
import com.glasspark.studyspot.common.CustomException;
import com.glasspark.studyspot.common.ErrorCode;
import com.glasspark.studyspot.members.domain.Member;
import com.glasspark.studyspot.members.dto.MemberJoinRequest;
import com.glasspark.studyspot.members.mapper.MemberMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<Void> join(MemberJoinRequest request) {
        if (memberMapper.countByUsername(request.getUsername()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }
        if (memberMapper.countByEmail(request.getEmail()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (memberMapper.countByNickname(request.getNickname()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        Member member = Member.createMember(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname()
        );

        try {
            memberMapper.insert(member);
        } catch (DuplicateKeyException exception) {
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER_INFO);
        }
        return ApiResponse.ok();
    }

    public ApiResponse<Void> delete(Long memberId) {
        if (memberMapper.withdrawMember(memberId) == 0) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return ApiResponse.ok();
    }
}
