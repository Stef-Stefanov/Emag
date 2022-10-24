package com.example.emag.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminDTO {
    @JsonProperty
    private boolean isAdmin;
    private String password;
    private String adminPassword;
}
