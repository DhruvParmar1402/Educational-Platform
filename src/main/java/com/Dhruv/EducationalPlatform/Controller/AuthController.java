package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Exception.BadRequest;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Groups.LoginGroup;
import com.Dhruv.EducationalPlatform.Groups.RegisterGroup;
import com.Dhruv.EducationalPlatform.Service.AuthService;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import com.Dhruv.EducationalPlatform.Util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private MessageSourceImpl messageSource;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated(RegisterGroup.class) @RequestBody UserDTO user) {
        ResponseHandler<String> response;
        try {
            authService.save(user);
            response = new ResponseHandler<>(null, messageSource.getMessage("user.saved.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BadRequest e) {
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.CONFLICT,false);
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        catch (Exception e) {
            response = new ResponseHandler<>(null,e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Validated(LoginGroup.class) @RequestBody UserDTO user) {
        ResponseHandler<String> response;
        try {
            response = new ResponseHandler<>(authService.login(user), "Jwt token", HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e)
        {
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.NOT_FOUND,false);
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}