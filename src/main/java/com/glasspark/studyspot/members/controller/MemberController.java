package com.glasspark.studyspot.members.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberController {

    // 로그인
    @GetMapping("/login")
    public String login() {
        return "members/login";
    }

    //회원가입
    @GetMapping("/join")
    public String join() {
        return "members/join";
    }



}
