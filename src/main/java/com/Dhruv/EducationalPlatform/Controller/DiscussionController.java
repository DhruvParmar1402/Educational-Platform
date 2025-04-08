package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.DTO.DiscussionDTO;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Util.PaginationResponse;
import com.Dhruv.EducationalPlatform.Util.ResponseHandler;
import com.Dhruv.EducationalPlatform.Groups.DiscussionGroup;
import com.Dhruv.EducationalPlatform.Service.DiscussionService;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discussion")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private MessageSourceImpl messageSource;


    @PostMapping
    public ResponseEntity<?> addDiscussion(@Validated(DiscussionGroup.class) @RequestBody DiscussionDTO discussion) {
        ResponseHandler<String> response;
        try {
            discussionService.save(discussion);
            response = new ResponseHandler<>(null, messageSource.getMessage("discussion.saved.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiscussionByCourseId(@PathVariable String id,@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey){
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse page=discussionService.findByCourseId(id,pageSize,lastEvaluatedKey);
            response = new ResponseHandler<>(page, messageSource.getMessage("discussion.found.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getDiscussionByUserId(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey) {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse page=discussionService.findByUserId(pageSize,lastEvaluatedKey);
            response = new ResponseHandler<>(page, messageSource.getMessage("discussion.found.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (EntityNotFound e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage() , HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}