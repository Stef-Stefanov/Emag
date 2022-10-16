package com.example.emag.model.dto;

import com.example.emag.model.exceptions.NotFoundException;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDTO{

    private int status;
    private LocalDateTime time;
    private String msg;
}
