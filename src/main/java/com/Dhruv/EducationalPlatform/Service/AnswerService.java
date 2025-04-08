package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.AnswerDTO;
import com.Dhruv.EducationalPlatform.DTO.PaginatedResponse;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Entity.AnswerEntity;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Repository.AnswerRepository;
import com.Dhruv.EducationalPlatform.Util.AuthenticatedUserProvider;
import com.Dhruv.EducationalPlatform.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;


    public void save(AnswerDTO answerDTO) throws EntityNotFound {
        UserDTO user = userService.findUserById(authenticatedUserProvider.getUserId());
        discussionService.findDiscussionById(answerDTO.getDiscussionId());

        answerDTO.setUserId(user.getId());
        answerRepository.save(answerDTO);
    }

    public PaginationResponse findByDiscussionId(String discussionId,int pageSize,String lastEvaluatedKey) {
        List<AnswerDTO> answers=answerRepository.findByDiscussionId(discussionId,pageSize,lastEvaluatedKey);
        boolean hasMore= !(answers.size() < pageSize);
        if(hasMore)
        {
            lastEvaluatedKey=answers.getLast().getId();
        }
        PaginationResponse paginationResponse=new PaginationResponse(answers,lastEvaluatedKey,pageSize,hasMore);
        return paginationResponse;

    }

    public PaginationResponse findByUserId(int pageSize,String lastEvaluatedKey) {
        String userId = authenticatedUserProvider.getUserId();
        List<AnswerDTO> answers= answerRepository.findByUserId(userId,pageSize,lastEvaluatedKey);
        boolean hasMore=!(answers.size() < pageSize);

        if(hasMore)
        {
            lastEvaluatedKey=answers.getLast().getId();
        }

        PaginationResponse paginationResponse=new PaginationResponse(answers,lastEvaluatedKey,pageSize,hasMore);
        return paginationResponse;
    }
}
