package com.example.emag.controller;

import com.example.emag.model.dto.user.LoginDTO;
import com.example.emag.model.dto.user.RegisterDTO;
import com.example.emag.model.dto.user.UserWithoutPassDTO;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController extends AbstractController{
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    // receive DTO, send to User Service, deserialize in UserService, map to entity, register, send back 200 ok.
    public RegisterDTO registerUser(@RequestBody RegisterDTO dto, HttpServletRequest req){
//        LoginDTO loginDTO = userService.checkForUser(dto);
//        UserWithoutPassDTO result = userService.login(loginDTO);
//        logUser(req, result.getId());
        return userService.registerUser(dto);
    }

    @PostMapping("/auth")
    public UserWithoutPassDTO login(@RequestBody LoginDTO dto, HttpSession s){
        UserWithoutPassDTO result = userService.login(dto);
        if(result != null){
            s.setAttribute("LOGGED", true);
            s.setAttribute("USER_ID", result.getId());
            return result;
        }
        else{
            throw new BadRequestException("Wrong Credentials");
        }
    }
    @GetMapping("/users/{uid}")
    public UserWithoutPassDTO getById(@PathVariable int uid){
        return userService.getById(uid);
    }
}
