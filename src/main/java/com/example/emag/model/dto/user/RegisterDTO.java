package com.example.emag.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;

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
    private String createdAt;

    /*
     =====================================
     Must be annotated with @JsonProperty
     so that Jackson uses the field name
     to determine the property name
     =====================================
     */
    @JsonProperty
    private boolean isSubscribed;
    @JsonProperty
    private boolean isAdmin;

    private String phoneNumber;
    private String birthDate;


}
