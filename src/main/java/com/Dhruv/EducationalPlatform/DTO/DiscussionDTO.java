package com.Dhruv.EducationalPlatform.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DiscussionDTO {

    private String id;

    @NotBlank(groups = DiscussionDTO.class,message = "{discussion.courseId.blank}")
    private String courseId;

    private String userId;

    @NotBlank(groups = DiscussionDTO.class, message = "{discussion.message.blank}")
    private String message;

    private Date createdAt;
}
