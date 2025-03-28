package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.AnswerDTO;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Repository.AnswerRepository;
import com.Dhruv.EducationalPlatform.Util.AuthenticatedUserProvider;
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

    public List<AnswerDTO> findByDiscussionId(String discussionId) {
        return answerRepository.findByDiscussionId(discussionId);
    }

    public List<AnswerDTO> findByUserId() {
        String userId = authenticatedUserProvider.getUserId();
        return answerRepository.findByUserId(userId);
    }
}
