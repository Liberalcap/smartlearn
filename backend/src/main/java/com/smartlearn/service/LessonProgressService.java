package com.smartlearn.service;

import com.smartlearn.entity.Lesson;
import com.smartlearn.entity.LessonProgress;
import com.smartlearn.entity.User;
import com.smartlearn.entity.UserRole;
import com.smartlearn.repository.CourseRepository;
import com.smartlearn.repository.EnrollmentRepository;
import com.smartlearn.repository.LessonProgressRepository;
import com.smartlearn.repository.LessonRepository;
import com.smartlearn.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonProgressService {

    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public LessonProgressService(
            LessonProgressRepository lessonProgressRepository,
            LessonRepository lessonRepository,
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository,
            UserRepository userRepository) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.lessonRepository = lessonRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public LessonProgress completeLesson(String studentEmail, Long lessonId) {
        User student = findStudent(studentEmail);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        Long courseId = lesson.getCourse().getId();
        if (!enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        if (lessonProgressRepository.existsByStudentIdAndLessonId(student.getId(), lessonId)) {
            throw new IllegalArgumentException("Lesson is already marked complete");
        }

        return lessonProgressRepository.save(new LessonProgress(student, lesson));
    }

    public List<LessonProgress> getCourseProgress(String studentEmail, Long courseId) {
        User student = findStudent(studentEmail);
        courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        return lessonProgressRepository.findByStudentIdAndLessonCourseId(student.getId(), courseId);
    }

    private User findStudent(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (user.getRole() != UserRole.STUDENT) {
            throw new IllegalArgumentException("Only students can update lesson progress");
        }

        return user;
    }
}
