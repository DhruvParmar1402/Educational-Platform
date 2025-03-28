package com.Dhruv.EducationalPlatform.DTO;

import com.Dhruv.EducationalPlatform.Groups.EnrollmentGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data

public class EnrollmentDTO {

    private String userId;

    @NotBlank(groups = EnrollmentGroup.class, message = "{enrollment.courseId.null}")
    private String courseId;
}
