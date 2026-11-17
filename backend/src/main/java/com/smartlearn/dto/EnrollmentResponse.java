package com.smartlearn.dto;

import com.smartlearn.entity.Enrollment;

public record EnrollmentResponse(
        Long id,
        Long courseId,
        String courseTitle,
        boolean completed
) {
    public static EnrollmentResponse from(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getTitle(),
                enrollment.isCompleted());
    }
}
