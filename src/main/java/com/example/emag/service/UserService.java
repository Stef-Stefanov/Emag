package com.example.emag.service;

import com.example.emag.model.dto.user.*;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.model.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService extends AbstractService{

    @Transactional
    public User registerUserService(RegisterDTO dto) {
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new BadRequestException("Passwords mismatch!");
        }
        //validate if exists and if format is suitable
        User u = modelMapper.map(dto, User.class);
        userRepository.save(u);
        return u;
    }

    public UserWithoutPassDTO login(LoginDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        if(!validateEmail(email) || !validatePassword(password)){
            throw new BadRequestException("Wrong credentials");
        }
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            User u = user.get();
            if (password.equals(u.getPassword())){
                return modelMapper.map(u, UserWithoutPassDTO.class);
            }
            throw new UnauthorizedException("Wrong credentials!  A");
        }
        else{
            throw new UnauthorizedException("Wrong credentials!  B");
        }
    }
    //=======================================================
    // Simple email validator. Doesn't work with numerical IP.
    // Should implement RFC 5322 standard from http://emailregex.com/ at some point ,
    // but the official standard regex has a known issue with Java.
    // todo
    private boolean validateEmail(String email){
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find()) {
            throw new BadRequestException("Email is not compliant");
        }
        return true;
    }

    /**=======================================================
    * The regex validates that a password contains at least one of each
    * lowercase, uppercase, digit and special chars and is between 4 and 12 chars.
    * The minimum is set at 4 to make testing easier.
    */
    private boolean validatePassword(String password){
        Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{4,12}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            throw new BadRequestException("Password is not compliant." +
                    " Must be between 4 and 12 chars containing one or more" +
                    " lowercase, uppercase, digit and special chars!");
        }
        return true;
    }

    /**
    * =======================================================
    * Validates correct birthdate. Accepts three mySQL accepted delimiters.
    * Todo needs to implement correct date and time logic -> no 32 of month 13.
    * Also year needs to be after 1723 as sql doesn't suport older years.
    *
    */
    private boolean validateBirthDate(String string){
        Pattern pattern = Pattern.compile("(?<y>[0-9]{4})([:\\-_])(?<m>[0-9]{2})\\2(?<d>[0-9]{2})");
        Matcher matcher = pattern.matcher(string);
        if (!matcher.find()) {
            throw new BadRequestException("Date of birth is not compliant");
        }
        return true;
    }
    //=======================================================
    // A public master method that calls the different validation methods of a full set UserDTO.
    public void validate(RegisterDTO dto){
        validatePassword(dto.getPassword());
        validateEmail(dto.getEmail());
        validateBirthDate(dto.getBirthDate());
    }
    //=======================================================
    // The next methods check if email is free to register with.
    public void checkEmailAvailability(String email) {
        Optional<User> result = userRepository.findByEmail(email);
        if (result.isPresent()) {
            throw new BadRequestException("Email is taken");
        }
    }

    public void deleteUser(HttpSession s) {
        checkIfLogged(s);
        userRepository.deleteById((long)s.getAttribute("USER_ID"));
    }

    private void checkIfLogged(HttpSession s){
        if (s==null || s.isNew()){
            s.setAttribute("LOGGED",false);
            throw new UnauthorizedException("You are not logged in! A");
        }
        if (!(boolean) s.getAttribute("LOGGED")){
            throw new UnauthorizedException("You are not logged in! B");
        }
    }

    public User registerUser(RegisterDTO dto, HttpServletRequest req){
        checkEmailAvailability(dto.getEmail());
        LoginDTO loginDTO = transformRegDtoIntoLoginDto(dto);
        validate(dto);
        User result = registerUserService(dto);
        login(loginDTO);
        return result;
    }
    private LoginDTO transformRegDtoIntoLoginDto(RegisterDTO rdto){
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(rdto.getEmail());
        loginDTO.setPassword(rdto.getPassword());
        return loginDTO;
    }
    public UserWithoutPassDTO getById(int uid) {
        // TODO: 22.10.2022 г.
        return null;
    }
}
