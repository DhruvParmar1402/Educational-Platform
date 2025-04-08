package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Exception.UnAuthorized;
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
    public ResponseEntity<?> addCourse(@Validated(CourseGroup.class) @RequestBody  CourseDTO courseDTO){
        ResponseHandler<Object> response;
        try
        {
            courseService.save(courseDTO);
            response=new ResponseHandler<>(null,messageSource.getMessage("course.saved.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.NOT_FOUND,false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            response=new ResponseHandler<>(null,e.getMessage() , HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping
    public ResponseEntity<?> getAllCreated()
    {
        ResponseHandler<List<CourseDTO>> response;
        try {
            List<CourseDTO> courseList=courseService.findAll();
            response=new ResponseHandler<>(courseList,messageSource.getMessage("course.found.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response=new ResponseHandler<>(null,e.getMessage() , HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id, @Validated(CourseGroup.class) @RequestBody CourseDTO courseDTO){
        ResponseHandler<Object> response;
        try {
            courseService.updateCourse(id, courseDTO);
            response= new ResponseHandler(null,messageSource.getMessage("course.update.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e)
        {
            response=new ResponseHandler<>(null,e.getMessage() , HttpStatus.NOT_FOUND,false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage() , HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        ResponseHandler<Object> response;

        try {
            courseService.deleteCourse(id);
            response=new ResponseHandler<>(null,messageSource.getMessage("course.deleted.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (UnAuthorized e)
        {
            response=new ResponseHandler<>(null,e.getMessage() , HttpStatus.UNAUTHORIZED,false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        catch (EntityNotFound e)
        {
            response=new ResponseHandler<>(null,e.getMessage() , HttpStatus.NOT_FOUND,false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage() , HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> viewAllCourses() {
        ResponseHandler<List<CourseDTO>> response;

        try {
            List<CourseDTO> list=courseService.getAllCourses();
            response=new ResponseHandler(list,messageSource.getMessage("course.fetched.success"),HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage() , HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
