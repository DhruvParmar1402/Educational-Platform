package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.DTO.EnrollmentDTO;
import com.Dhruv.EducationalPlatform.Util.ResponseHandler;
import com.Dhruv.EducationalPlatform.Groups.EnrollmentGroup;
import com.Dhruv.EducationalPlatform.Service.EnrollmentService;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enroll")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private MessageSourceImpl messageSource;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<?> enroll(@Validated(EnrollmentGroup.class) @RequestBody EnrollmentDTO enrollmentDTO) throws Exception {
        ResponseHandler<String> response;
        try {
            enrollmentService.save(enrollmentDTO);
            response=new ResponseHandler<>(null,messageSource.getMessage("enrollment.saved.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            response=new ResponseHandler<>(null,messageSource.getMessage("enrollment.saved.fail") , HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllEnrolled() throws Exception {
        ResponseHandler<List<CourseDTO>> response;
        try {
            response=new ResponseHandler<>(enrollmentService.findById(),messageSource.getMessage("enrollment.found.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response=new ResponseHandler<>(null,messageSource.getMessage("enrollment.found.fail") , HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
