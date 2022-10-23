package com.example.emag.controller;

import com.example.emag.model.dto.user.LoginDTO;
import com.example.emag.model.dto.user.RegisterDTO;
import com.example.emag.model.dto.user.UserWithoutPassDTO;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.model.exceptions.UnauthorizedException;
import com.example.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController extends AbstractController{
    @Autowired
    private UserService userService;

    // 1. receive DTO,
    // 2.1 Prepare Login DTO 2.2 Check username and pass viability 2.3 Check availability
    // 3.1 Register user 3.2 Login user
    @PostMapping("/users")
    public RegisterDTO registerUser(@RequestBody RegisterDTO dto, HttpServletRequest req){
        LoginDTO loginDTO = userService.checkForUser(dto);
        userService.validate(dto); //todo might pottentially move the validate inside the service.
        User result = userService.registerUserService(dto);
        userService.login(loginDTO);
        logUser(req, result.getId());
        return dto;
    }

    @PostMapping("/auth")
    public UserWithoutPassDTO login(@RequestBody LoginDTO dto, HttpSession s, HttpServletRequest req){
        UserWithoutPassDTO result = userService.login(dto);
        if(result != null){
            s.setAttribute("LOGGED", true);
            s.setAttribute("USER_ID", result.getId());
            s.setAttribute("REMOTE_IP", req.getRemoteAddr());
            return result;
        }
        else{
            throw new BadRequestException("Wrong Credentials");
        }
    }
    /*
        check if logged
        if not logged - you have to be logged
        if logged - log out
     */
    @PutMapping("/exit")
    public void logout(HttpSession s) {
        if (s==null || s.isNew()){
            s.setAttribute("LOGGED",false);
            throw new UnauthorizedException("You are not logged in! A");
        }
        if (!(boolean) s.getAttribute("LOGGED")){
            throw new UnauthorizedException("You are not logged in! B");
        }
        s.setAttribute("LOGGED",false);
    }
    @GetMapping("/users/{uid}")
    public UserWithoutPassDTO getById(@PathVariable int uid){
        return userService.getById(uid);
    }
}
