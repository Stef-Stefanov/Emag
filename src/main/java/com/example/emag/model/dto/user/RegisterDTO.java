package com.example.emag.model.dto.user;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;

@Data
public class RegisterDTO {
    @NotNull
    private String email;
    @NotNull
    private String password;
//    @NotNull
    private String confirmPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String createdAt;
    // must be set up as not null but not from Client
//    @Column(name = "is_subscribed")
    private boolean isSubscribed;
    // must be set up as not null but not from Client
//    @Column(name = "is_admin")
    private boolean isAdmin;
    private String phoneNumber;
    private String birthDate;


}
