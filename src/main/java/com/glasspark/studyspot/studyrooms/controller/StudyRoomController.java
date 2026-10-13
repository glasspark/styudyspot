package com.glasspark.studyspot.studyrooms.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/studyrooms")
public class StudyRoomController {

    // 스터디룸 목록 화면. 데이터는 화면의 JS 가 /api/studyroom 으로 불러온다.
    @GetMapping
    public String list() {
        return "studyrooms/list";
    }

    // 스터디룸 상세 + 예약 화면
    @GetMapping("/{studyRoomId}")
    public String detail(@PathVariable Long studyRoomId, Model model) {
        model.addAttribute("studyRoomId", studyRoomId);
        return "studyrooms/detail";
    }

}
