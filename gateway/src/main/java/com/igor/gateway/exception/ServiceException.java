package com.igor.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ServiceException extends Exception{
    private HttpStatus httpStatus;
    private String code;
    private String message;

}
