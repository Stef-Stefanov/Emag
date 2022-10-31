package com.example.emag.controller;

import com.example.emag.model.dto.cart.UserHasProductsInCardWithoutUserIdDTO;
import com.example.emag.model.dto.user.*;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.model.exceptions.UnauthorizedException;
import com.example.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController extends AbstractController{
    // todo махни тази проверка
    @PostMapping("/users")
    public UserWithoutPassDTO registerUser(@RequestBody RegisterDTO dto, HttpServletRequest req){
        if (checkIfLoggedBoolean(req.getSession())){
            throw new BadRequestException("You are already logged in");
        }
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new BadRequestException("Passwords mismatch!");
        }
        UserWithoutPassDTO withoutPassDTO = userService.registerUser(dto);
        logUser(req, withoutPassDTO.getId());
        return withoutPassDTO;
    }
    @Transactional
    @DeleteMapping("/users/delete")
    public void deleteUser(@RequestBody LoginDTO dto, HttpServletRequest req){
        validateSession(req);
        userService.deleteUser(dto, (long) req.getSession().getAttribute("USER_ID"));
        req.getSession().invalidate();
    }
    @PostMapping("/users/login")
    public UserWithoutPassDTO login(@RequestBody LoginDTO dto, HttpServletRequest req){
        if (checkIfLoggedBoolean(req.getSession())){
            throw new BadRequestException("You are already logged in!");
        }
        UserWithoutPassDTO result = userService.loginUser(dto);
        if(result != null){
            req.getSession().setAttribute(LOGGED, true);
            req.getSession().setAttribute(USER_ID, result.getId());
            req.getSession().setAttribute(REMOTE_IP, req.getRemoteAddr());
            return result;
        }
        else{
            throw new BadRequestException("Wrong Credentials");
        }
    }
    @PutMapping("/users/logout")
    public void logout(HttpSession s) {
        s.invalidate();
    }

    @PutMapping("/users/edit")
    public void updateUserDate(@RequestBody UpdateProfileDTO dto, HttpServletRequest req){
        validateSession(req);
        userService.updateUserInfo(dto, (long)req.getSession().getAttribute("USER_ID"));
    }
    @PutMapping("/users/editPass")
    public void updateUserPass(@RequestBody ChangePassDTO dto, HttpServletRequest req){
        validateSession(req);
        userService.updatePass(dto, (long)req.getSession().getAttribute(USER_ID));
    }
    @PutMapping("/users/admin")
    public void setAdminPrivileges(@RequestBody AdminDTO dto, HttpServletRequest req){
        validateSession(req);
        userService.makeAdmin(dto, (long)req.getSession().getAttribute(USER_ID));
    }
    @PostMapping("/admin/pass")
    public String lookUpMasterAdminPassword(@RequestBody LoginDTO dto, HttpServletRequest req){
        validateSession(req);
        return userService.lookUpAdminPassword(dto,(long)req.getSession().getAttribute("USER_ID"));
    }
}
