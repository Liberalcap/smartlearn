package com.smartlearn.service;

import com.smartlearn.entity.Course;
import com.smartlearn.entity.Lesson;
import com.smartlearn.entity.User;
import com.smartlearn.repository.CourseRepository;
import com.smartlearn.repository.LessonRepository;
import com.smartlearn.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public LessonService(
            LessonRepository lessonRepository,
            CourseRepository courseRepository,
            UserRepository userRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public Lesson createLesson(
            String instructorEmail,
            Long courseId,
            String title,
            String content,
            Integer position) {
        Course course = findOwnedCourse(instructorEmail, courseId);
        Lesson lesson = new Lesson(title, content, position, course);
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonsForCourse(Long courseId) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        return lessonRepository.findByCourseIdOrderByPositionAsc(courseId);
    }

    public Lesson updateLesson(
            String instructorEmail,
            Long courseId,
            Long lessonId,
            String title,
            String content,
            Integer position) {
        findOwnedCourse(instructorEmail, courseId);
        Lesson lesson = lessonRepository.findByIdAndCourseId(lessonId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        lesson.setTitle(title);
        lesson.setContent(content);
        lesson.setPosition(position);
        return lessonRepository.save(lesson);
    }

    public void deleteLesson(String instructorEmail, Long courseId, Long lessonId) {
        findOwnedCourse(instructorEmail, courseId);
        Lesson lesson = lessonRepository.findByIdAndCourseId(lessonId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
        lessonRepository.delete(lesson);
    }

    private Course findOwnedCourse(String instructorEmail, Long courseId) {
        User instructor = userRepository.findByEmail(instructorEmail)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new IllegalArgumentException("You do not own this course");
        }

        return course;
    }
}
