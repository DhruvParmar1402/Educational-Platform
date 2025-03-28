package com.Dhruv.EducationalPlatform.Util;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter

public class ResponseHandler<T>{
    private T data;
    private String message;
    private HttpStatus status;
    private boolean success;
}
