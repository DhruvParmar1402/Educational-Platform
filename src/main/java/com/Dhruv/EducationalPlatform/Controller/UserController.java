package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Util.PaginationResponse;
import com.Dhruv.EducationalPlatform.Util.ResponseHandler;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Groups.RegisterGroup;
import com.Dhruv.EducationalPlatform.Service.UserService;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService authService;

    @Autowired
    private MessageSourceImpl messageSource;


    @PutMapping
    public ResponseEntity<?> updateUser(@Validated(RegisterGroup.class) @RequestBody UserDTO user) {
        ResponseHandler<String> response;
        try {
            authService.update(user);
            response = new ResponseHandler<>(null, messageSource.getMessage("user.updated.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e) {
            response = new ResponseHandler<>(null, e.getMessage() , HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage() , HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> viewPersonalDetails() {
        ResponseHandler<UserDTO> response;
        try {
            UserDTO user=authService.getDetails();
            response = new ResponseHandler<>(user, messageSource.getMessage("user.found.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll (@RequestParam(defaultValue = "5") int pageSize,@RequestParam(required = false) String lastEvaluatedKey) {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse paginationResponse = authService.getAll(pageSize, lastEvaluatedKey);
            response=new ResponseHandler<>(paginationResponse,messageSource.getMessage("user.found.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
