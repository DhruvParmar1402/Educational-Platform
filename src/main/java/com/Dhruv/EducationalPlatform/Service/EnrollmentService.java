package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.DTO.EnrollmentDTO;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Entity.EnrollmentEntity;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Repository.EnrollmentRepository;
import com.Dhruv.EducationalPlatform.Util.AuthenticatedUserProvider;
import com.Dhruv.EducationalPlatform.Util.JwtUtil;
import com.Dhruv.EducationalPlatform.Util.PaginationResponse;
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
        UserDTO user =userService.findUserById(authenticatedUserProvider.getUserId());
        courseService.findCourseById(enrollmentDTO.getCourseId());
        enrollmentDTO.setUserId(user.getId());
        enrollmentRepository.save(enrollmentDTO);
    }

    public PaginationResponse findById(int pageSize,String lastEvaluatedKey) throws EntityNotFound {
        UserDTO user = userService.findUserById(authenticatedUserProvider.getUserId());

        List<EnrollmentDTO> list = enrollmentRepository.findAll(user.getId(),pageSize,lastEvaluatedKey);

        boolean hasMore=!(list.size()<pageSize);
        if(hasMore)
        {
            lastEvaluatedKey=list.getLast().getCourseId();
        }
        PaginationResponse paginationResponse=new PaginationResponse(list,lastEvaluatedKey,pageSize,hasMore);
        return paginationResponse;
    }

}
