package com.Dhruv.EducationalPlatform.Controller;


import com.Dhruv.EducationalPlatform.DTO.EnrollmentDTO;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Util.PaginationResponse;
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


@RestController
@RequestMapping("/enroll")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private MessageSourceImpl messageSource;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<?> enroll(@Validated(EnrollmentGroup.class) @RequestBody EnrollmentDTO enrollmentDTO) {
        ResponseHandler<String> response;
        try {
            enrollmentService.save(enrollmentDTO);
            response=new ResponseHandler<>(null,messageSource.getMessage("enrollment.saved.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.NOT_FOUND,true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            response=new ResponseHandler<>(null,e.getMessage() , HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllEnrolled(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey) {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse page=enrollmentService.findById(pageSize,lastEvaluatedKey);
            response=new ResponseHandler<>(page,messageSource.getMessage("enrollment.found.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e) {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.NOT_FOUND,true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            response=new ResponseHandler<>(null,e.getMessage() , HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
