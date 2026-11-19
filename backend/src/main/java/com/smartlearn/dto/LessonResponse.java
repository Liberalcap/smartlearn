package com.smartlearn.dto;

import com.smartlearn.entity.Lesson;

public record LessonResponse(
        Long id,
        String title,
        String content,
        Integer position,
        Long courseId
) {
    public static LessonResponse from(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getContent(),
                lesson.getPosition(),
                lesson.getCourse().getId());
    }
}
