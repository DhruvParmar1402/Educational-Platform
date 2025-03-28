package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.DTO.AnswerDTO;
import com.Dhruv.EducationalPlatform.Util.ResponseHandler;
import com.Dhruv.EducationalPlatform.Groups.AnswerGroup;
import com.Dhruv.EducationalPlatform.Service.AnswerService;
import com.Dhruv.EducationalPlatform.Util.MessageSourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private MessageSourceImpl messageSource;

    @PostMapping
    public ResponseEntity<?> save(@Validated(AnswerGroup.class) @RequestBody AnswerDTO answerDTO) {
        ResponseHandler<String> response;
        try{
            response=new ResponseHandler<>(null,messageSource.getMessage("answer.saved.success") , HttpStatus.OK,true);
            answerService.save(answerDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response=new ResponseHandler<>(null,messageSource.getMessage("answer.saved.fail") , HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{discussionId}")
    public ResponseEntity<?> findByDiscussionId(@PathVariable String discussionId)
    {
        ResponseHandler<List<AnswerDTO>> response;
        try {
            response=new ResponseHandler<>(answerService.findByDiscussionId(discussionId),messageSource.getMessage("answer.found.success") , HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,messageSource.getMessage("answer.found.fail") , HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @GetMapping
    public ResponseEntity<?> findByUserId()
    {
        ResponseHandler<List<AnswerDTO>> response;
        try {
            response=new ResponseHandler<>(answerService.findByUserId(),messageSource.getMessage("answer.found.success") , HttpStatus.OK,true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,messageSource.getMessage("answer.found.fail") , HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
