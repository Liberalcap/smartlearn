package com.smartlearn.dto;

import com.smartlearn.entity.LessonProgress;

public record LessonProgressResponse(
        Long id,
        Long lessonId,
        String lessonTitle,
        boolean completed
) {
    public static LessonProgressResponse from(LessonProgress progress) {
        return new LessonProgressResponse(
                progress.getId(),
                progress.getLesson().getId(),
                progress.getLesson().getTitle(),
                progress.isCompleted());
    }
}
