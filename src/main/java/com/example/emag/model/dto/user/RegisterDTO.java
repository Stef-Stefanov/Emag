package com.example.emag.model.dto.user;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String createdAt;
    // must be set up as not null but not from Client
    private boolean isSubscribed;
    // must be set up as not null but not from Client
    private boolean isAdmin;
    private String phoneNumber;
    private String birthDate;


}
