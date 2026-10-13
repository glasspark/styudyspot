package com.glasspark.studyspot.studyrooms.controller;

import com.glasspark.studyspot.common.ApiResponse;
import com.glasspark.studyspot.common.PageResponse;
import com.glasspark.studyspot.studyrooms.dto.StudyRoomRegisterRequest;
import com.glasspark.studyspot.studyrooms.dto.StudyRoomResponse;
import com.glasspark.studyspot.studyrooms.dto.StudyRoomSearchRequest;
import com.glasspark.studyspot.studyrooms.service.StudyRoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/studyroom")
public class StudyRoomApiController {

    private StudyRoomService studyRoomService;

    @Operation(summary = "스터디룸 등록", description = "스터디룸 정보를 받아 신규 스터디룸을 등록")
    @PostMapping
    public ApiResponse<Void> createStudyRoom(@Valid @RequestBody StudyRoomRegisterRequest request) {
        return studyRoomService.createStudyRoom(request);
    }

    @Operation(summary = "스터디룸 리스트 반환", description = "등록된 스터디룸 리스트를 반환")
    @GetMapping
    public ApiResponse<PageResponse<StudyRoomResponse>> getStudyRoomList(@Valid StudyRoomSearchRequest request) {
        return studyRoomService.getStudyRoomList(request);
    }

    @Operation(summary = "스터디룸 상세", description = "스터디룸 한 건의 정보를 반환 (폐쇄된 룸은 404)")
    @GetMapping("/{studyRoomId}")
    public ApiResponse<StudyRoomResponse> getStudyRoom(@PathVariable Long studyRoomId) {
        return studyRoomService.getStudyRoom(studyRoomId);
    }


}
