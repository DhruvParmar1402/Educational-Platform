package com.Dhruv.EducationalPlatform.Service;

import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Util.JwtUtil;
import com.Dhruv.EducationalPlatform.Exception.BadCredentials;
import com.Dhruv.EducationalPlatform.Exception.BadRequest;
import com.Dhruv.EducationalPlatform.Repository.UserRepository;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSourceImpl messageSource;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    public void save(UserDTO user) throws Exception {
        if (userService.findUserByEmail(user.getEmail()) != null) {
            throw new BadRequest(messageSource.getMessage("user.email.exists"));
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String login(UserDTO user) throws Exception {
        UserDTO userDetails = userService.findUserByEmail(user.getEmail());
        if (userDetails == null) {
            throw new BadCredentials(messageSource.getMessage("user.email.notExists"));
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails.getId(), user.getPassword()));
        return jwtUtil.generateToken(userDetails.getId(), userDetails.getRole());
    }
}
