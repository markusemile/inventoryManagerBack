package com.markdev.inventoryManagmentsSystem.controller;

import com.markdev.inventoryManagmentsSystem.dto.LoginRequest;
import com.markdev.inventoryManagmentsSystem.dto.RegisterRequest;
import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody @Valid LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

}
