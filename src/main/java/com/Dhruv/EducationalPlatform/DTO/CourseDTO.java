package com.Dhruv.EducationalPlatform.DTO;

import com.Dhruv.EducationalPlatform.Groups.CourseGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data

public class CourseDTO {
    private String id;

    @NotBlank(groups = CourseGroup.class,message = "{course.title.blank}")
    private String title;

    @NotBlank(groups = CourseGroup.class,message = "{course.description.blank}")
    private String description;

    private String instructorId;

    private Date createdAt=new Date();
}
