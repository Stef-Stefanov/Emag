package com.example.emag.service;

import com.example.emag.model.dto.cart.UserHasProductsInCardWithoutUserIdDTO;
import com.example.emag.model.dto.order.OrderWithoutOwnerDTO;
import com.example.emag.model.dto.user.*;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.model.exceptions.GoneEntityException;
import com.example.emag.model.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService{
    public UserWithoutPassDTO loginUser(LoginDTO dto) {
        String email = dto.getEmail();

        if( ! validateEmail(email) || ! validatePassword(dto.getPassword())){
            throw new BadRequestException("Wrong credentials");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Wrong credentials!  B"));

        if (passwordEncoder.matches(dto.getPassword(),user.getPassword())){
            return transformUserIntoUserWithoutPassDTO(user);
        }
        throw new UnauthorizedException("Wrong credentials!  A");
    }

    @Transactional
    public UserWithoutPassDTO registerUser(RegisterDTO dto){

        validate(dto);
        checkEmailAvailability(dto.getEmail());

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User result = modelMapper.map(dto, User.class);
        result.setSubscribed(true);
        result.setAdmin(false);
        result.setCreatedAt(LocalDateTime.now());
        userRepository.save(result);

        LoginDTO loginDTO = transformRegisterDtoIntoLoginDto(dto);

        return loginUser(loginDTO);
    }

    public void updateUserInfo(UpdateProfileDTO dto, long userID){
        User u = userRepository.findById(userID)
                .orElseThrow(()->new GoneEntityException("No such user!"));
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
    public void updatePass(ChangePassDTO dto, long userID){
        if ( ! dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new BadRequestException("New password mismatch");
        }
        validatePassword(dto.getNewPassword());
        User u = checkCredentials(dto,userID);
        u.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(u);
    }

    public void makeAdmin(AdminDTO dto, long userID){
        User u = checkCredentials(dto, userID);
        if (! dto.getAdminPassword().equals(adminPassword)) {
            throw new UnauthorizedException("Wrong credentials! Q");
        }
        u.setAdmin(dto.isAdmin());
        userRepository.save(u);
    }

    public String lookUpAdminPassword(LoginDTO dto, long userID) {
        User u = checkCredentials(dto, userID);
        if (!u.isAdmin()){
            throw new UnauthorizedException("You are not an administrator!");
        }
        return adminPassword;
        // Additionally the pass can be encrypted alongside the user IP for increased security,
        // so that the password can be shared only with users on the same network.
    }


    public void deleteUser(LoginDTO dto, long userID) {
        if (!checkLoginCredentialsToUserID(dto.getEmail(), userID)) {
            throw new UnauthorizedException("Bad Credentials! 45");
        }
        loginUser(dto);
        userRepository.deleteById(userID);

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
    private boolean validateBirthDate(LocalDate birthDate){
        if (birthDate.getYear() < 1800 || birthDate.getYear() > 2100 || birthDate.isAfter(LocalDate.now())) {
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
    private boolean checkLoginCredentialsToUserID(String email, long userID){
           return userRepository.findById(userID)
                   .orElseThrow(() -> new GoneEntityException("Bad credentials! 54"))
                   .getEmail().equals(email);
    }

    private LoginDTO transformRegisterDtoIntoLoginDto(RegisterDTO rdto){
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(rdto.getEmail());
        loginDTO.setPassword(rdto.getPassword());
        return loginDTO;
    }
    private UserWithoutPassDTO transformUserIntoUserWithoutPassDTO(User u){
        UserWithoutPassDTO dto = modelMapper.map(u,UserWithoutPassDTO.class);
        if(u.getPastOrders() == null){
            dto.setPastOrders(null);
        }
        else {
            dto.setPastOrders(u.getPastOrders().stream()
                    .map(o -> modelMapper.map(o, OrderWithoutOwnerDTO.class))
                    .collect(Collectors.toList()));
        }
        return dto;
    }
    public User checkCredentials(AdminDTO dto, long userID){
        User u = userRepository.findById(userID).orElseThrow();
        if (!dto.getPassword().equals(u.getPassword())
                && !dto.getPassword().equals(u.getPassword())){
            throw new UnauthorizedException("Wrong credentials! D");
        }
        return u;
    }
    public User checkCredentials(LoginDTO dto, long userID){
        User u = userRepository.findById(userID).orElseThrow();
        if (!dto.getPassword().equals(u.getPassword())
                && !dto.getEmail().equals(u.getEmail())){
            throw new UnauthorizedException("Wrong credentials! D");
        }
        return u;
    }
    public User checkCredentials(ChangePassDTO dto, long userID){
        User u = userRepository.findById(userID).orElseThrow();
        if (!dto.getPassword().equals(u.getPassword())
                && !dto.getPassword().equals(u.getPassword())){
            throw new UnauthorizedException("Wrong credentials! D");
        }
        return u;
    }
    public boolean checkIfAdminUserId(long uid){
        return userRepository.findById(uid)
                .orElseThrow(()-> new GoneEntityException("No such user!"))
                .isAdmin();
    }
    public UserWithoutPassDTO getById(Long uid) {
        User u = userRepository.findById(uid).orElseThrow(() -> new BadRequestException("No such user found"));
        return transformUserIntoUserWithoutPassDTO(u);
    }

    public UserOrderHistoryDTO getOrderHistory(long userid) {
        UserWithoutPassDTO dto = getById(userid);
        return modelMapper.map(dto,UserOrderHistoryDTO.class);
    }
    public UserCartDTO getCart(long userid) {
        UserWithoutPassDTO dto = getById(userid);
        return modelMapper.map(dto,UserCartDTO.class);
    }
    public UserFavoritesDTO getFavorites(long userid) {
        UserWithoutPassDTO dto = getById(userid);
        return modelMapper.map(dto,UserFavoritesDTO.class);
    }
}
