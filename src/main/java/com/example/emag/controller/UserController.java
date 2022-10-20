package com.example.emag.controller;

import com.example.emag.model.dto.user.LoginDTO;
import com.example.emag.model.dto.user.UserDTO;
import com.example.emag.model.repositories.UserRepository;
import com.example.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController extends AbstractController{
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public UserDTO addUser(@RequestBody LoginDTO dto, HttpServletRequest req){
        UserDTO result = userService.login(dto);
        logUser(req, result.getId());
        return userService.add(dto);
    }
}
