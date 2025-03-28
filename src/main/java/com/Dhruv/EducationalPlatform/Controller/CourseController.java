package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.Util.ResponseHandler;
import com.Dhruv.EducationalPlatform.Groups.CourseGroup;
import com.Dhruv.EducationalPlatform.Service.CourseService;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private MessageSourceImpl messageSource;

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<?> addCourse(@Validated(CourseGroup.class) @RequestBody  CourseDTO courseDTO) throws Exception {
        ResponseHandler<String> response;
        try
        {
            courseService.save(courseDTO);
            response=new ResponseHandler<>(null,messageSource.getMessage("course.saved.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response=new ResponseHandler<>(null,messageSource.getMessage("course.saved.fail") , HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @GetMapping
    public ResponseEntity<?> getAll()
    {
        ResponseHandler<List<CourseDTO>> response;
        try {
            response=new ResponseHandler<>(courseService.findAll(),messageSource.getMessage("course.found.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response=new ResponseHandler<>(null,messageSource.getMessage("course.found.failure") , HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
