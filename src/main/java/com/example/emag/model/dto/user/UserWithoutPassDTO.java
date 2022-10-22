package com.example.emag.model.dto.user;

import lombok.Data;

@Data
public class UserWithoutPassDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String createdAt;
    private int isSubscribed;
    private int isAdmin;
    private String phoneNumber;
    private String birthDate;
}
