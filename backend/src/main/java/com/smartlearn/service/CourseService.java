package com.smartlearn.service;

import com.smartlearn.entity.Course;
import com.smartlearn.entity.User;
import com.smartlearn.entity.UserRole;
import com.smartlearn.repository.CourseRepository;
import com.smartlearn.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public Course createCourse(String instructorEmail, String title, String description) {
        User instructor = findInstructor(instructorEmail);
        return courseRepository.save(new Course(title, description, instructor));
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getInstructorCourses(String instructorEmail) {
        User instructor = findInstructor(instructorEmail);
        return courseRepository.findByInstructorId(instructor.getId());
    }

    private User findInstructor(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        if (user.getRole() != UserRole.INSTRUCTOR) {
            throw new IllegalArgumentException("Only instructors can manage courses");
        }

        return user;
    }
}
