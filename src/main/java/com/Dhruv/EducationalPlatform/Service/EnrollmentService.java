package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.DTO.EnrollmentDTO;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Entity.EnrollmentEntity;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Repository.EnrollmentRepository;
import com.Dhruv.EducationalPlatform.Util.AuthenticatedUserProvider;
import com.Dhruv.EducationalPlatform.Util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    ModelMapper mapper = new ModelMapper();


    public void save(EnrollmentDTO enrollmentDTO) throws EntityNotFound {
        UserDTO user =userService.findUserById(authenticatedUserProvider.getUserId()) ;
        courseService.findCourseById(enrollmentDTO.getCourseId());
        enrollmentDTO.setUserId(user.getId());
        enrollmentRepository.save(enrollmentDTO);
    }

    public List<CourseDTO> findById() throws EntityNotFound {
        UserDTO user = userService.findUserById(authenticatedUserProvider.getUserId());


        List<EnrollmentDTO> enrollmentDTOS = enrollmentRepository.findAll(user.getId());
        List<CourseDTO> courseDTOS = new ArrayList<>();

        for (EnrollmentDTO dto : enrollmentDTOS) {
            CourseDTO course = courseService.findCourseById(dto.getCourseId());
            courseDTOS.add(course);
        }

        return courseDTOS;
    }

}
