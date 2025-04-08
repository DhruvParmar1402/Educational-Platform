package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Repository.UserRepository;
import com.Dhruv.EducationalPlatform.Util.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private MessageSourceImpl messageSource;

    @Autowired
    private PasswordEncoder encoder;


    public void update(UserDTO userDTO) throws EntityNotFound {
        String userId=authenticatedUserProvider.getUserId();
        UserDTO user=findUserById(userId);

        if(user==null)
        {
            throw  new EntityNotFound("user.email.notExists");
        }

        userDTO.setId(userId);
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        userRepository.save(userDTO);
    }

    public UserDTO getDetails() throws EntityNotFound {
        String userId=authenticatedUserProvider.getUserId();
        return findUserById(userId);
    }

    public UserDTO  findUserByEmail(String email) {
        UserDTO user=userRepository.findUserByEmail(email);
        return user;
    }

    public UserDTO findUserById(String id) throws EntityNotFound {
        UserDTO user=userRepository.findUserById(id);
        if(user==null)
        {
            throw new EntityNotFound(messageSource.getMessage("user.email.notExists"));
        }
        return user;
    }

    public PaginationResponse getAll(int pageSize, String lastEvaluatedKey) {
        ScanResult result = userRepository.getAll(lastEvaluatedKey, pageSize);

        String nextLastEvaluatedKey = null;
        if (result.getLastEvaluatedKey() != null && result.getLastEvaluatedKey().containsKey("id")) {
            nextLastEvaluatedKey = result.getLastEvaluatedKey().get("id").getS();
        }

        List<UserDTO> userList = ScanResultToDTO.convert(result);
        boolean hasMore = nextLastEvaluatedKey != null;
        return new PaginationResponse(userList, nextLastEvaluatedKey, pageSize, hasMore);
    }
}
