package com.smartlearn.dto;

public record CourseProgressResponse(
        Long courseId,
        long completedLessons,
        long totalLessons,
        int percentage
) {
}
