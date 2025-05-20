package com.markdev.inventoryManagmentsSystem.service.impl;

import com.markdev.inventoryManagmentsSystem.dto.LoginRequest;
import com.markdev.inventoryManagmentsSystem.dto.RegisterRequest;
import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.dto.UserDto;
import com.markdev.inventoryManagmentsSystem.entity.User;
import com.markdev.inventoryManagmentsSystem.enums.UserRole;
import com.markdev.inventoryManagmentsSystem.exceptions.DuplicateValueException;
import com.markdev.inventoryManagmentsSystem.exceptions.InvalidCredentialsException;
import com.markdev.inventoryManagmentsSystem.exceptions.NotFoundException;
import com.markdev.inventoryManagmentsSystem.repository.UserRepository;
import com.markdev.inventoryManagmentsSystem.security.JwUtils;
import com.markdev.inventoryManagmentsSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwUtils jwUtils;



    @Override
    public Response registerUser(RegisterRequest registerRequest) {

        UserRole role = UserRole.MANAGER;

        if(registerRequest.getRole() != null){
            role = registerRequest.getRole();
        }

        User userToSave = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(role)
                .build();

        Optional<User> existingUser = userRepository.findUserByEmail(registerRequest.getEmail());
        if(existingUser.isPresent()) throw new DuplicateValueException(String.format("Email is already registered.%nPlease log-in !"));

        userRepository.save(userToSave);


        return Response.builder()
                .status(200)
                .message("User created successfully")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository.findUserByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new NotFoundException("User not found"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Password does not match");
        }

        String token = jwUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("User logged in successfully")
                .role(user.getRole())
                .token(token)
                .expirationTime("24 hours")
                .build();
    }
    @Override
    public Response getAllUsers() {

        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));

        List<UserDto> userDTOS = modelMapper.map(users, new TypeToken<List<UserDto>>() {}.getType());

        userDTOS.forEach(userDto->userDto.setTransactions(null));

        return Response.builder()
                .status(200)
                .message("success")
                .users(userDTOS)
                .build();

    }


    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        user.setTransactions(null);
        return user;
    }


    @Override
    public Response updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User not found"));

        if(userDto.getEmail()!=null) existingUser.setEmail(userDto.getEmail());
        if(userDto.getName()!=null) existingUser.setName(userDto.getName());
        if(userDto.getRole()!=null) existingUser.setRole(userDto.getRole());
        if(userDto.getPhoneNumber()!=null) existingUser.setPhoneNumber(userDto.getPhoneNumber());

        if(userDto.getPassword()!=null && !userDto.getPassword().isBlank()){
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User was successfully updated")
                .build();
    }

    @Override
    public Response deleteUser(Long id) {

        userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User not found"));

        userRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("User successfully deleted")
                .build();
    }

    @Override
    public Response getUserTransaction(Long id) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User not found"));
        UserDto userDto = modelMapper.map(existingUser,UserDto.class);
        userDto.getTransactions().forEach(transactionDto -> {
            transactionDto.setUser(null);
            transactionDto.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("Success")
                .build();
    }
}
