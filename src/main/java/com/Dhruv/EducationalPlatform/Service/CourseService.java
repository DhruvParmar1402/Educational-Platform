package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Entity.CourseEntity;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Repository.CourseRepository;
import com.Dhruv.EducationalPlatform.Util.AuthenticatedUserProvider;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import com.Dhruv.EducationalPlatform.Util.PaginationResponse;
import com.Dhruv.EducationalPlatform.Util.ScanResultToDTO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    private ModelMapper mapper = new ModelMapper();

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSourceImpl messageSource;


    public void save(CourseDTO courseDTO) throws EntityNotFound {
        UserDTO user = userService.findUserById(authenticatedUserProvider.getUserId());
        courseDTO.setInstructorId(user.getId());
        courseRepository.save(courseDTO);
    }

    public List<CourseDTO> findAll() {
        String userId = authenticatedUserProvider.getUserId();
        return courseRepository.getAll(userId).stream().map((course) -> mapper.map(course, CourseDTO.class)).toList();
    }

    public CourseDTO findCourseById(String id) throws EntityNotFound {
        CourseDTO course = courseRepository.findCourseById(id);
        if (course == null) {
            throw new EntityNotFound(messageSource.getMessage("course.found.failure"));
        }
        return course;
    }

    public void updateCourse(String id, CourseDTO courseDTO) throws EntityNotFound {
        CourseDTO existingCourse = findCourseById(id);

        String currentUserId = authenticatedUserProvider.getUserId();
        if (!existingCourse.getInstructorId().equals(currentUserId)) {
            throw new RuntimeException("You can only update your own courses.");
        }

        courseDTO.setInstructorId(currentUserId);
        courseDTO.setId(existingCourse.getId());
        courseRepository.save(courseDTO);
    }

    public void deleteCourse(String id) throws EntityNotFound {
        CourseDTO existingCourse = findCourseById(id);
        String currentUserId = authenticatedUserProvider.getUserId();
        if (existingCourse == null || !existingCourse.getInstructorId().equals(currentUserId)) {
            throw new RuntimeException("You can only delete your own courses.");
        }
        findCourseById(id);
        courseRepository.deleteCourse(mapper.map(existingCourse, CourseEntity.class));
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.getAllCourses();
    }


}
