package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Repository.CourseRepository;
import com.Dhruv.EducationalPlatform.Util.AuthenticatedUserProvider;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

}
