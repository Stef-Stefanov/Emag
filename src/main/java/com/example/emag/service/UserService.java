package com.example.emag.service;

import com.example.emag.model.dto.user.LoginDTO;
import com.example.emag.model.dto.user.RegisterDTO;
import com.example.emag.model.dto.user.UserWithoutPassDTO;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.model.exceptions.UnauthorizedException;
import com.example.emag.model.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService extends AbstractService{

    @Transactional
    public RegisterDTO registerUser(RegisterDTO dto) {
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new BadRequestException("Passwords mismatch!");
        }
        //validate if exists and if format is suitable
        User u = modelMapper.map(dto, User.class);
        System.out.println(u.isAdmin()); // todo delete
        userRepository.save(u);
        System.out.println(dto.isAdmin()); // todo delete
        return dto;
    }


    public UserWithoutPassDTO login(LoginDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        if(!validateUsername(email) || !validatePassword(password)){
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
    private boolean validateUsername(String username){
        return true;
    }
    private boolean validatePassword(String username){
        return true;
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
