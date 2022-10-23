package com.example.emag.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.GONE)
public class GoneEntityException extends RuntimeException{
    public GoneEntityException(String message){
        super(message);
    }
}
