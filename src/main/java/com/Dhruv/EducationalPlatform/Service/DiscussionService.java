package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.DTO.DiscussionDTO;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Repository.DiscussionRepository;
import com.Dhruv.EducationalPlatform.Util.AuthenticatedUserProvider;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSourceImpl messageSource;


    public void save(DiscussionDTO discussion) throws EntityNotFound {
        UserDTO user = userService.findUserById(authenticatedUserProvider.getUserId());
        courseService.findCourseById(discussion.getCourseId());

        discussion.setUserId(user.getId());
        discussionRepository.save(discussion);
    }

    public List<DiscussionDTO> findByCourseId(String courseId) throws EntityNotFound {
        CourseDTO course = courseService.findCourseById(courseId);
        return discussionRepository.findByCourseId(course.getId());
    }


    public List<DiscussionDTO> findByUserId() throws EntityNotFound {
        UserDTO user = userService.findUserById(authenticatedUserProvider.getUserId());
        return discussionRepository.findByUserId(user.getId());
    }

    public DiscussionDTO findDiscussionById(String id) throws EntityNotFound {
        DiscussionDTO discussionDTO = discussionRepository.findById(id);
        if (discussionDTO == null) {
            throw new EntityNotFound("discussion.found.fail");
        }
        return discussionDTO;
    }
}
