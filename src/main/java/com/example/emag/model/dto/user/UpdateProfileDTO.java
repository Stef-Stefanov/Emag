package com.example.emag.model.dto.user;

import lombok.Data;

@Data
public class UpdateProfileDTO {

    private String email;
    private String firstName;
    private String lastName;
    private boolean isSubscribed;
    private String phoneNumber;
    private String birthDate;

}
