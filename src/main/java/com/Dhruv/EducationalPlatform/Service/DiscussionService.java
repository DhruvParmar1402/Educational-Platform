package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.DTO.DiscussionDTO;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Repository.DiscussionRepository;
import com.Dhruv.EducationalPlatform.Util.AuthenticatedUserProvider;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import com.Dhruv.EducationalPlatform.Util.PaginationResponse;
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

    public PaginationResponse findByCourseId(String courseId,int pageSize,String lastEvaluatedKey) throws EntityNotFound {
        CourseDTO course = courseService.findCourseById(courseId);
        List<DiscussionDTO> list=discussionRepository.findByCourseId(course.getId(),pageSize,lastEvaluatedKey);
        boolean hasMore=!(list.size()<pageSize);
        if(hasMore)
        {
            lastEvaluatedKey=list.getLast().getId();
        }
        return new PaginationResponse(list,lastEvaluatedKey,pageSize,hasMore);
    }


    public PaginationResponse findByUserId(int pageSize, String lastEvaluatedKey) throws EntityNotFound {
        UserDTO user = userService.findUserById(authenticatedUserProvider.getUserId());
        List<DiscussionDTO> list=discussionRepository.findByUserId(user.getId(),pageSize,lastEvaluatedKey);
        boolean hasMore=!(list.size()<pageSize);
        if(hasMore)
        {
            lastEvaluatedKey=list.getLast().getId();
        }
        PaginationResponse page=new PaginationResponse(list,lastEvaluatedKey,pageSize,hasMore);

        return page;
    }

    public DiscussionDTO findDiscussionById(String id) throws EntityNotFound {
        DiscussionDTO discussionDTO = discussionRepository.findById(id);
        if (discussionDTO == null) {
            throw new EntityNotFound(messageSource.getMessage("discussion.found.fail"));
        }
        return discussionDTO;
    }
}
