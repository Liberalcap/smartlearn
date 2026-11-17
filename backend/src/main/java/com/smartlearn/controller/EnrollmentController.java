package com.smartlearn.controller;

import com.smartlearn.dto.ApiResponse;
import com.smartlearn.dto.EnrollmentResponse;
import com.smartlearn.entity.Enrollment;
import com.smartlearn.service.EnrollmentService;
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
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/courses/{courseId}")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enroll(
            @PathVariable Long courseId,
            Authentication authentication) {
        Enrollment enrollment = enrollmentService.enroll(authentication.getName(), courseId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Enrolled successfully", EnrollmentResponse.from(enrollment)));
    }

    @GetMapping("/me")
    public ApiResponse<List<EnrollmentResponse>> getMyEnrollments(Authentication authentication) {
        List<EnrollmentResponse> enrollments = enrollmentService
                .getStudentEnrollments(authentication.getName())
                .stream()
                .map(EnrollmentResponse::from)
                .toList();

        return ApiResponse.success("Enrollments retrieved successfully", enrollments);
    }
}
