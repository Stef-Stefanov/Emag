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

    @PostMapping("/users")
    public RegisterDTO registerUser(@RequestBody RegisterDTO dto, HttpServletRequest req){
        if (checkIfLoggedBoolean(req.getSession())){
            throw new BadRequestException("You are already logged in");
        }
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new BadRequestException("Passwords mismatch!");
        }
        User result = userService.registerUser(dto);
        logUser(req, result.getId());
        return dto;
    }
    @Transactional
    @DeleteMapping("/end")
    public void deleteUser(@RequestBody LoginDTO dto, HttpServletRequest req){
        checkIfLogged(req);
        checkIpWithSessionIp(req);
        userService.deleteUser(dto, (long) req.getSession().getAttribute("USER_ID"));
        req.getSession().invalidate();
    }
    @PostMapping("/auth")
    public UserWithoutPassDTO login(@RequestBody LoginDTO dto, HttpServletRequest req){
        if (checkIfLoggedBoolean(req.getSession())){
            throw new BadRequestException("You are already logged in!");
        }
        UserWithoutPassDTO result = userService.loginUser(dto);
        if(result != null){
            req.getSession().setAttribute("LOGGED", true);
            req.getSession().setAttribute("USER_ID", result.getId());
            req.getSession().setAttribute("REMOTE_IP", req.getRemoteAddr());
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
    public void updateUserDate(@RequestBody UpdateProfileDTO dto, HttpServletRequest req){
        checkIfLogged(req);
        checkIpWithSessionIp(req);
        userService.updateUserInfo(dto, (long)req.getSession().getAttribute("USER_ID"));
    }
    @PutMapping("/secure")
    public void updateUserPass(@RequestBody ChangePassDTO dto, HttpServletRequest req){
        checkIfLogged(req);
        checkIpWithSessionIp(req);
        userService.updatePass(dto, (long)req.getSession().getAttribute(USER_ID));
    }
    @PutMapping("/upgrade")
    public void giveAdminPrivileges(@RequestBody AdminDTO dto, HttpServletRequest req){
        checkIfLogged(req);
        checkIpWithSessionIp(req);
        userService.makeAdmin(dto, (long)req.getSession().getAttribute(USER_ID));
    }
    @PostMapping("/priv")
    public String lookUpMasterAdminPassword(@RequestBody LoginDTO dto, HttpServletRequest req){
        checkIfLogged(req);
        checkIpWithSessionIp(req);
        return userService.lookUpAdminPassword(dto,(long)req.getSession().getAttribute("USER_ID"));
    }
    @GetMapping("/orders/history")
    public UserOrderHistoryDTO lookUpUserOrderHistory(HttpServletRequest req){
        checkIfLogged(req);
        return userService.getOrderHistory((long) req.getSession().getAttribute("USER_ID"));
    }
    @GetMapping("/orders/cart")
    public UserCartDTO lookUpUserCart(HttpServletRequest req){
        checkIfLogged(req);
        return userService.getCart((long) req.getSession().getAttribute("USER_ID"));
    }
    @GetMapping("/orders/favorites")
    public UserFavoritesDTO lookUpUserFavorites(HttpServletRequest req){
        checkIfLogged(req);
        return userService.getFavorites((long) req.getSession().getAttribute("USER_ID"));
    }
}
