package com.smartlearn.controller;

import com.smartlearn.dto.ApiResponse;
import com.smartlearn.dto.LessonRequest;
import com.smartlearn.dto.LessonResponse;
import com.smartlearn.entity.Lesson;
import com.smartlearn.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(
            @PathVariable Long courseId,
            @Valid @RequestBody LessonRequest request,
            Authentication authentication) {
        Lesson lesson = lessonService.createLesson(
                authentication.getName(),
                courseId,
                request.title(),
                request.content(),
                request.position());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lesson created successfully", LessonResponse.from(lesson)));
    }

    @GetMapping
    public ApiResponse<List<LessonResponse>> getLessons(@PathVariable Long courseId) {
        List<LessonResponse> lessons = lessonService.getLessonsForCourse(courseId)
                .stream()
                .map(LessonResponse::from)
                .toList();

        return ApiResponse.success("Lessons retrieved successfully", lessons);
    }

    @PutMapping("/{lessonId}")
    public ApiResponse<LessonResponse> updateLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonRequest request,
            Authentication authentication) {
        Lesson lesson = lessonService.updateLesson(
                authentication.getName(),
                courseId,
                lessonId,
                request.title(),
                request.content(),
                request.position());

        return ApiResponse.success("Lesson updated successfully", LessonResponse.from(lesson));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            Authentication authentication) {
        lessonService.deleteLesson(authentication.getName(), courseId, lessonId);
        return ResponseEntity.ok(ApiResponse.success("Lesson deleted successfully", null));
    }
}
