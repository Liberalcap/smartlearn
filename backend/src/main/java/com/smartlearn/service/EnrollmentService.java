package com.smartlearn.service;

import com.smartlearn.entity.Course;
import com.smartlearn.entity.Enrollment;
import com.smartlearn.entity.User;
import com.smartlearn.entity.UserRole;
import com.smartlearn.repository.CourseRepository;
import com.smartlearn.repository.EnrollmentRepository;
import com.smartlearn.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            UserRepository userRepository,
            CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public Enrollment enroll(String studentEmail, Long courseId) {
        User student = findStudent(studentEmail);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

        return enrollmentRepository.save(new Enrollment(student, course));
    }

    public List<Enrollment> getStudentEnrollments(String studentEmail) {
        User student = findStudent(studentEmail);
        return enrollmentRepository.findByStudentId(student.getId());
    }

    private User findStudent(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (user.getRole() != UserRole.STUDENT) {
            throw new IllegalArgumentException("Only students can manage enrollments");
        }

        return user;
    }
}
