package com.smartlearn.controller;

import com.smartlearn.dto.ApiResponse;
import com.smartlearn.dto.CourseProgressResponse;
import com.smartlearn.dto.LessonProgressResponse;
import com.smartlearn.entity.LessonProgress;
import com.smartlearn.repository.LessonRepository;
import com.smartlearn.service.LessonProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class LessonProgressController {

    private final LessonProgressService lessonProgressService;
    private final LessonRepository lessonRepository;

    public LessonProgressController(
            LessonProgressService lessonProgressService,
            LessonRepository lessonRepository) {
        this.lessonProgressService = lessonProgressService;
        this.lessonRepository = lessonRepository;
    }

    @PostMapping("/lessons/{lessonId}/complete")
    public ResponseEntity<ApiResponse<LessonProgressResponse>> completeLesson(
            @PathVariable Long lessonId,
            Authentication authentication) {
        LessonProgress progress = lessonProgressService.completeLesson(
                authentication.getName(),
                lessonId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lesson marked complete", LessonProgressResponse.from(progress)));
    }

    @GetMapping("/courses/{courseId}")
    public ApiResponse<CourseProgressResponse> getCourseProgress(
            @PathVariable Long courseId,
            Authentication authentication) {
        List<LessonProgress> progress = lessonProgressService.getCourseProgress(
                authentication.getName(),
                courseId);
        long totalLessons = lessonRepository.findByCourseIdOrderByPositionAsc(courseId).size();
        long completedLessons = progress.size();
        int percentage = totalLessons == 0
                ? 0
                : (int) Math.round((completedLessons * 100.0) / totalLessons);

        return ApiResponse.success(
                "Course progress retrieved successfully",
                new CourseProgressResponse(courseId, completedLessons, totalLessons, percentage));
    }
}
