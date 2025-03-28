package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.Util.PaginationResponse;
import com.Dhruv.EducationalPlatform.Util.ResponseHandler;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Groups.LoginGroup;
import com.Dhruv.EducationalPlatform.Groups.RegisterGroup;
import com.Dhruv.EducationalPlatform.Service.UserService;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService authService;

    @Autowired
    private MessageSourceImpl messageSource;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated(RegisterGroup.class) @RequestBody UserDTO user) {
        ResponseHandler<String> response;
        try {
            authService.save(user);
            response = new ResponseHandler<>(null, messageSource.getMessage("user.saved.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, messageSource.getMessage("user.saved.fail"), HttpStatus.INTERNAL_SERVER_ERROR, false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Validated(LoginGroup.class) @RequestBody UserDTO user) {
        ResponseHandler<String> response;
        try {
            response = new ResponseHandler<>(authService.login(user), "Jwt token", HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, messageSource.getMessage("user.login.fail"), HttpStatus.INTERNAL_SERVER_ERROR, false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Validated(RegisterGroup.class) @RequestBody UserDTO user) {
        ResponseHandler<String> response;
        try {
            authService.update(user);
            response = new ResponseHandler<>(null, messageSource.getMessage("user.updated.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, messageSource.getMessage("user.update.fail"), HttpStatus.INTERNAL_SERVER_ERROR, false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @GetMapping
    public ResponseEntity<?> viewPersonalDetails() {
        ResponseHandler<UserDTO> response;
        try {
            response = new ResponseHandler<>(authService.getDetails(), messageSource.getMessage("user.found.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, messageSource.getMessage("user.found.failure"), HttpStatus.INTERNAL_SERVER_ERROR, false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "5") int pageSize,@RequestParam(required = false) String lastEvaluatedKey) {
        try {
            PaginationResponse paginationResponse = authService.getAll(pageSize, lastEvaluatedKey);
            return ResponseEntity.ok(new ResponseHandler<>(paginationResponse, "Users retrieved successfully", HttpStatus.OK, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseHandler<>(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
        }
    }

}
