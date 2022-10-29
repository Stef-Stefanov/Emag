package com.example.emag.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileDTO {

    private String email;
    private String firstName;
    private String lastName;
    /*
     =====================================
     Must be annotated with @JsonProperty
     so that Jackson uses the field name
     to determine the property name
     =====================================
     */
    @JsonProperty
    private boolean isSubscribed;
    private String phoneNumber;
    private LocalDate birthDate;

}
