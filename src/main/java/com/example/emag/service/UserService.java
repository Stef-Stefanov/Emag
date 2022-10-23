package com.example.emag.service;

import com.example.emag.model.dto.user.LoginDTO;
import com.example.emag.model.dto.user.RegisterDTO;
import com.example.emag.model.dto.user.UserWithoutPassDTO;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.model.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public UserWithoutPassDTO getById(int uid) {
        // TODO: 22.10.2022 г.
        return null;
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
    public LoginDTO checkForUser(RegisterDTO dto) {
        Optional<User> result = userRepository.findByEmail(dto.getEmail());
        if (result.isPresent()) {
            throw new BadRequestException("Email is taken");

        } else {
            return modelMapper.map(dto,LoginDTO.class);
        }
    }
}
