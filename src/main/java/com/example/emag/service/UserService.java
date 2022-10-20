package com.example.emag.service;

import com.example.emag.model.dto.user.LoginDTO;
import com.example.emag.model.dto.user.UserDTO;
import com.example.emag.model.entities.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends AbstractService{

    public UserDTO add(LoginDTO u) {
        return modelMapper.map(u, UserDTO.class);
    }


    public UserDTO login(LoginDTO dto) {
        return modelMapper.map(dto, UserDTO.class);
    }
}
