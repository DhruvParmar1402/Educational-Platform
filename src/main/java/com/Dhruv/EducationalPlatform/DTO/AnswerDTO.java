package com.Dhruv.EducationalPlatform.DTO;

import com.Dhruv.EducationalPlatform.Groups.AnswerGroup;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AnswerDTO {

    private String id;

    @NotNull(groups = AnswerGroup.class ,message = "{answer.discussionId.null}")
    private String discussionId;

    private String userId;

    @NotNull(groups = AnswerGroup.class,message = "{answer.message.null}")
    private String message;

    private Date date=new Date();
}
