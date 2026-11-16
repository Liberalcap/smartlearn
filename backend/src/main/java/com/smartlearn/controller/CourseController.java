package com.smartlearn.controller;

import com.smartlearn.dto.ApiResponse;
import com.smartlearn.dto.CourseRequest;
import com.smartlearn.dto.CourseResponse;
import com.smartlearn.entity.Course;
import com.smartlearn.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CourseRequest request,
            Authentication authentication) {
        Course course = courseService.createCourse(
                authentication.getName(),
                request.title(),
                request.description());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully", CourseResponse.from(course)));
    }

    @GetMapping
    public ApiResponse<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses()
                .stream()
                .map(CourseResponse::from)
                .toList();

        return ApiResponse.success("Courses retrieved successfully", courses);
    }

    @GetMapping("/mine")
    public ApiResponse<List<CourseResponse>> getMyCourses(Authentication authentication) {
        List<CourseResponse> courses = courseService.getInstructorCourses(authentication.getName())
                .stream()
                .map(CourseResponse::from)
                .toList();

        return ApiResponse.success("Instructor courses retrieved successfully", courses);
    }
}
