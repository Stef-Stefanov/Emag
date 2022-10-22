package com.example.emag.model.dto.user;

import lombok.Data;

@Data
public class ChangePassDTO {

    private String password;
    private String newPassword;
    private String confirmNewPassword;
}
