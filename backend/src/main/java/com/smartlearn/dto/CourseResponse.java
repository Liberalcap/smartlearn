package com.smartlearn.dto;

import com.smartlearn.entity.Course;

public record CourseResponse(
        Long id,
        String title,
        String description,
        Long instructorId,
        String instructorName
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getInstructor().getId(),
                course.getInstructor().getName());
    }
}
