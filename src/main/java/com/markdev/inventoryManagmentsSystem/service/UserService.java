package com.markdev.inventoryManagmentsSystem.service;

import com.markdev.inventoryManagmentsSystem.dto.LoginRequest;
import com.markdev.inventoryManagmentsSystem.dto.RegisterRequest;
import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.dto.UserDto;
import com.markdev.inventoryManagmentsSystem.entity.User;

public interface UserService {

    Response registerUser(RegisterRequest registerRequest);
    Response loginUser(LoginRequest loginRequest);

    User getCurrentLoggedInUser();
    Response getAllUsers();
    Response updateUser(Long id, UserDto userDto);
    Response deleteUser(Long id);
    Response getUserTransaction(Long id);





}
