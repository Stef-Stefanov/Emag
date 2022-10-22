package com.example.emag.model.dto.user;

import lombok.Data;

@Data
public class EditProfileDTO {

    private String email;
    private String password;
    private Long id;
    private String firstName;
    private String lastName;
    private String createdAt;
    private boolean isSubscribed;
    private boolean isAdmin;
    private String phoneNumber;
    private String birthDate;

}
