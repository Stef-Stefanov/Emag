package com.example.emag.service;

import com.example.emag.model.dto.user.*;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.model.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService extends AbstractService{
    public UserWithoutPassDTO loginUser(LoginDTO dto) {
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
    @Transactional
    public User registerUser(RegisterDTO dto, HttpSession s ){
        if (checkIfLoggedBoolean(s)){
            throw new BadRequestException("You are already logged in");
        }
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new BadRequestException("Passwords mismatch!");
        }
        checkEmailAvailability(dto.getEmail());
        LoginDTO loginDTO = transformRegisterDtoIntoLoginDto(dto);
        validate(dto);
        User result = modelMapper.map(dto, User.class);
        result.setSubscribed(true);
        result.setAdmin(false);
        result.setCreatedAt(LocalDateTime.now());
        userRepository.save(result);
        loginUser(loginDTO);
        return result;
    }

    public void updateUserInfo(UpdateProfileDTO dto, HttpSession s){
        // todo what if user doesn't exist? -> GoneException
        // todo fix when no changes -> throw exc
        checkIfLogged(s);
        User u = userRepository.findById((long)s.getAttribute("USER_ID")).orElseThrow();
        if (!dto.getEmail().equals(u.getEmail())) {
            checkEmailAvailability(dto.getEmail());
            validateEmail(dto.getEmail());
            u.setEmail(dto.getEmail());
        }
        if (!dto.getPhoneNumber().equals(u.getPhoneNumber())) {
            u.setPhoneNumber(dto.getPhoneNumber());
        }
        if (!dto.getBirthDate().equals(u.getBirthDate())) {
            validateBirthDate(dto.getBirthDate());
            u.setBirthDate(dto.getBirthDate());
        }
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setSubscribed(dto.isSubscribed());
        userRepository.save(u);
    }
    public void updatePass(ChangePassDTO dto, HttpSession s){
        checkIfLogged(s);
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new BadRequestException("New password mismatch");
        }
        validatePassword(dto.getNewPassword());
        User u = checkCredentials(dto,s);
        u.setPassword(dto.getNewPassword());
        userRepository.save(u);
    }

    public void makeAdmin(AdminDTO dto, HttpSession s){
        checkIfLogged(s);
        User u = checkCredentials(dto, s);
        if (! dto.getAdminPassword().equals(adminPassword)) {
            throw new UnauthorizedException("Wrong credentials! Q");
        }
        u.setAdmin(dto.isAdmin());
        userRepository.save(u);
    }

    public String lookUpAdminPassword(LoginDTO dto, HttpSession s) {
        checkIfLogged(s);
        User u = checkCredentials(dto,s);
        if (!u.isAdmin()){
            throw new UnauthorizedException("You are not an administrator!");
        }
        //todo needs to be crypted with IP
        return adminPassword;
    }

    public void deleteUser(LoginDTO dto, HttpSession s) {
        checkIfLogged(s);
        matchEmailToSessionUserID(dto.getEmail(), s);
        loginUser(dto);
        userRepository.deleteById((long)s.getAttribute("USER_ID"));
        s.invalidate();
    }
    /**=======================================================
     A public master method that calls the different validation methods of a full set UserDTO.
     */
    public void validate(RegisterDTO dto){
        validatePassword(dto.getPassword());
        validateEmail(dto.getEmail());
        validateBirthDate(dto.getBirthDate());
    }
    /**=======================================================
     The regex validates that a password contains at least one of each
     lowercase, uppercase, digit and special chars and is between 4 and 12 chars.
     The minimum is set at 4 to make testing easier.
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
     =======================================================
     Simple email validator. Doesn't work with numerical IP.
     Should implement RFC 5322 standard from http://emailregex.com/ at some point ,
     but the official standard regex has a known issue with Java.
     todo
     */
    private boolean validateEmail(String email){
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find()) {
            throw new BadRequestException("Email is not compliant");
        }
        return true;
    }

    /**
     =======================================================
     Validates correct birthdate. Accepts three mySQL accepted delimiters.
     Todo needs to implement correct date and time logic -> no 32 of month 13.
     Also year needs to be after 1723 as sql doesn't support older years.
    */
    private boolean validateBirthDate(String string){
        Pattern pattern = Pattern.compile("(?<y>[0-9]{4})([:\\-_])(?<m>[0-9]{2})\\2(?<d>[0-9]{2})");
        Matcher matcher = pattern.matcher(string);
        if (!matcher.find()) {
            throw new BadRequestException("Date of birth is not compliant");
        }
        return true;
    }
    /**=======================================================
     The next methods check if email is free to register with.
    */
    private void checkEmailAvailability(String email) {
        Optional<User> result = userRepository.findByEmail(email);
        if (result.isPresent()) {
            throw new BadRequestException("Email is taken");
        }
    }
    private boolean checkIfLogged(HttpSession s){
        if (s.isNew()){
            s.setAttribute("LOGGED",false);
            throw new UnauthorizedException("You are not logged in! A");
        }
        if (!(boolean) s.getAttribute("LOGGED")){
            throw new UnauthorizedException("You are not logged in! B");
        }
        return true;
    }
    public boolean checkIfLoggedBoolean(HttpSession s){
        if(null == s.getAttribute("LOGGED")){
            s.setAttribute("LOGGED",false);
        }
        return (boolean) s.getAttribute("LOGGED");
    }
    private boolean matchEmailToSessionUserID(String email, HttpSession session){
        if (null != session.getAttribute("USER_ID")) {
            boolean result;
            try {
                result = email
                        .equals(userRepository
                                .findById((long)session.getAttribute("USER_ID")).orElseThrow().getEmail());
            } catch (NoSuchElementException e) {
                throw new UnauthorizedException("Wrong credentials! T");
            }
            return result;
        }
        return false;
    }

    private LoginDTO transformRegisterDtoIntoLoginDto(RegisterDTO rdto){
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(rdto.getEmail());
        loginDTO.setPassword(rdto.getPassword());
        return loginDTO;
    }
    public User checkCredentials(AdminDTO dto, HttpSession s){
        User u = userRepository.findById((long)s.getAttribute("USER_ID")).orElseThrow();
        if (!dto.getPassword().equals(u.getPassword())
                && !dto.getPassword().equals(u.getPassword())){
            throw new UnauthorizedException("Wrong credentials! D");
        }
        return u;
    }
    public User checkCredentials(LoginDTO dto, HttpSession s){
        User u = userRepository.findById((long)s.getAttribute("USER_ID")).orElseThrow();
        if (!dto.getPassword().equals(u.getPassword())
                && !dto.getEmail().equals(u.getEmail())){
            throw new UnauthorizedException("Wrong credentials! D");
        }
        return u;
    }
    public User checkCredentials(ChangePassDTO dto, HttpSession s){
        User u = userRepository.findById((long)s.getAttribute("USER_ID")).orElseThrow();
        if (!dto.getPassword().equals(u.getPassword())
                && !dto.getPassword().equals(u.getPassword())){
            throw new UnauthorizedException("Wrong credentials! D");
        }
        return u;
    }
    public UserWithoutPassDTO getById(Long uid) {
        try {
            User u = userRepository.findById(uid).orElseThrow();
            return modelMapper.map(u,UserWithoutPassDTO.class);
        } catch (RuntimeException e) {
            throw new BadRequestException("No such user found");
        }
    }
}
