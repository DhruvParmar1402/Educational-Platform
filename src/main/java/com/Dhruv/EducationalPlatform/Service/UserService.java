package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Exception.BadCredentials;
import com.Dhruv.EducationalPlatform.Exception.BadRequest;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Repository.UserRepository;
import com.Dhruv.EducationalPlatform.Util.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private UserDetailsProvider userDetailsService;

    @Autowired
    private MessageSourceImpl messageSource;

    private ModelMapper mapper = new ModelMapper();

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    public void save(UserDTO user) throws Exception {
        if (findUserByEmail(user.getEmail()) != null) {
            throw new BadRequest(messageSource.getMessage("user.email.exists"));
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String login(UserDTO user) throws Exception {
        UserDTO userDetails = findUserByEmail(user.getEmail());
        if (userDetails == null) {
            throw new BadCredentials(messageSource.getMessage("user.email.notexists"));
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails.getId(), user.getPassword()));
        return jwtUtil.generateToken(userDetails.getId(), userDetails.getRole());
    }

    public void update(UserDTO userDTO) {
        String userId=authenticatedUserProvider.getUserId();
        userDTO.setId(userId);
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
            throw new EntityNotFound(messageSource.getMessage("user.email.notexists"));
        }
        return user;
    }

    public PaginationResponse getAll(int pageSize, String lastEvaluatedKey) {
        Map<String, AttributeValue> startKey = null;
        if (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty()) {
            startKey = new HashMap<>();
            startKey.put("id", new AttributeValue().withS(lastEvaluatedKey));
        }
        ScanResult result = userRepository.getAll(startKey, pageSize);
        String nextLastEvaluatedKey = null;
        if (result.getLastEvaluatedKey() != null && result.getLastEvaluatedKey().containsKey("id")) {
            nextLastEvaluatedKey = result.getLastEvaluatedKey().get("id").getS();
        }
        List<UserDTO> userList = ScanResultToDTO.convert(result);
        boolean hasMore = nextLastEvaluatedKey != null;
        return new PaginationResponse(userList, nextLastEvaluatedKey, pageSize, hasMore);
    }


}
