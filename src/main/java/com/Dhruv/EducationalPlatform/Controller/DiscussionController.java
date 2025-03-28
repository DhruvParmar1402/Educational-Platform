package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.DTO.DiscussionDTO;
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
    public ResponseEntity<?> addDiscussion(@Validated(DiscussionGroup.class) @RequestBody DiscussionDTO discussion) throws Exception {
        ResponseHandler<String> response;
        try {
            discussionService.save(discussion);
            response=new ResponseHandler<>(null,messageSource.getMessage("discussion.saved.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
       } catch (Exception e) {
            response=new ResponseHandler<>(null,messageSource.getMessage("discussion.saved.fail") , HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
       }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiscussionByCourseId(@PathVariable String id) throws Exception
    {
        ResponseHandler<List<DiscussionDTO> > response;
        try {
            response=new ResponseHandler<>(discussionService.findByCourseId(id),messageSource.getMessage("discussion.found.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,messageSource.getMessage("discussion.found.fail") , HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getDiscussionByUserId() throws Exception {
        ResponseHandler<List<DiscussionDTO> > response;
        try {
            response=new ResponseHandler<>(discussionService.findByUserId(),messageSource.getMessage("discussion.found.success"), HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
       } catch (Exception e) {
            response=new ResponseHandler<>(null,messageSource.getMessage("discussion.found.fail") , HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
