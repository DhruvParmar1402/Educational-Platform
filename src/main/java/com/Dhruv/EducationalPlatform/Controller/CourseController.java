package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Util.PaginationResponse;
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
        } catch (EntityNotFound e) {
            response=new ResponseHandler<>(null,messageSource.getMessage("course.save.entityNotFound"), HttpStatus.NOT_FOUND,true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
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

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id, @Validated(CourseGroup.class) @RequestBody CourseDTO courseDTO) throws Exception {
        courseService.updateCourse(id, courseDTO);
        return ResponseEntity.ok(new ResponseHandler<>(null, "Course updated successfully", HttpStatus.OK, true));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) throws Exception {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(new ResponseHandler<>(null, "Course deleted successfully", HttpStatus.OK, true));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.ok(new ResponseHandler<>(courseService.getAllCourses(), "All courses retrieved successfully", HttpStatus.OK, true));
    }


}
