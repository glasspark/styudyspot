package com.glasspark.studyspot.members.mapper;

import com.glasspark.studyspot.members.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    Member findActiveByEmail(@Param("email") String email);

    Member findByUsername(@Param("username") String username);

    int countByUsername(@Param("username") String username);

    int countByEmail(@Param("email") String email);

    int countByNickname(@Param("nickname") String nickname);

    int withdrawMember(@Param("memberId") Long memberId);

    void insert(Member member);

}
