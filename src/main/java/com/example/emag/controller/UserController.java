package com.example.emag.controller;

import com.example.emag.model.dto.user.*;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.model.exceptions.UnauthorizedException;
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
    public RegisterDTO registerUser(@RequestBody RegisterDTO dto, HttpServletRequest req){
        User result = userService.registerUser(dto, req.getSession());
        logUser(req, result.getId());
        return dto;
    }
    @DeleteMapping("/end")
    public void deleteUser(@RequestBody LoginDTO dto, HttpServletRequest req){
        userService.deleteUser(dto, req.getSession());
    }
    @PostMapping("/auth")
    public UserWithoutPassDTO login(@RequestBody LoginDTO dto, HttpSession s, HttpServletRequest req){
        if (userService.checkIfLoggedBoolean(s)){
            throw new BadRequestException("You are already logged in!");
        }
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

    @PutMapping("/update")
    public void updateUserDate(@RequestBody UpdateProfileDTO dto, HttpSession s){
        userService.updateData(dto, s);
    }
    @PutMapping("/secure")
    public void updateUserPass(@RequestBody ChangePassDTO dto, HttpSession s){
        userService.updatePass(dto, s);
    }

}
