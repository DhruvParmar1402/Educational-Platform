package com.Dhruv.EducationalPlatform.DTO;

import com.Dhruv.EducationalPlatform.Groups.LoginGroup;
import com.Dhruv.EducationalPlatform.Groups.RegisterGroup;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {

    private String id;

    @NotBlank(message = "{user.name.notBlank}",groups = {RegisterGroup.class})
    private String name;

    @NotBlank(groups = {LoginGroup.class, RegisterGroup.class},message = "{user.password.notBlank}")
    private String password;

    @NotBlank(groups = {LoginGroup.class, RegisterGroup.class}, message = "{user.email.notBlank}")
    private String email;

    @NotBlank(groups = {RegisterGroup.class})
    @Pattern(regexp = "^(STUDENT|INSTRUCTOR)$", message = "{user.role.invalid}" ,groups = {RegisterGroup.class})
    private String role;

    private Date createdAt=new Date();
}
