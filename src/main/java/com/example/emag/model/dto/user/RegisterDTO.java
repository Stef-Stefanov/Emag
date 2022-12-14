package com.example.emag.model.dto.user;

import com.sun.istack.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterDTO {
    @NotNull
    private String email;
    @NotNull
    private String password;
    private String confirmPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;


}
