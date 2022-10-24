package com.example.emag.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateProfileDTO {

    private String email;
    private String firstName;
    private String lastName;
    @JsonProperty
    private boolean isSubscribed;
    private String phoneNumber;
    private String birthDate;

}
