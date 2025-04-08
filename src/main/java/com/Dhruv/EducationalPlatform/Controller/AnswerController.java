package com.Dhruv.EducationalPlatform.Controller;

import com.Dhruv.EducationalPlatform.DTO.AnswerDTO;
import com.Dhruv.EducationalPlatform.Exception.EntityNotFound;
import com.Dhruv.EducationalPlatform.Util.PaginationResponse;
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
        try {
            answerService.save(answerDTO);
            response = new ResponseHandler<>(null, messageSource.getMessage("answer.saved.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFound e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.NOT_FOUND, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{discussionId}")
    public ResponseEntity<?> findByDiscussionId(@PathVariable String discussionId,@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey) {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse answers=answerService.findByDiscussionId(discussionId,pageSize,lastEvaluatedKey);
            response = new ResponseHandler<>(answers, messageSource.getMessage("answer.found.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> findByUserId(@RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String lastEvaluatedKey) {
        ResponseHandler<PaginationResponse> response;
        try {
            PaginationResponse answers=answerService.findByUserId(pageSize,lastEvaluatedKey);
            response = new ResponseHandler<>(answers, messageSource.getMessage("answer.found.success"), HttpStatus.OK, true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new ResponseHandler<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
