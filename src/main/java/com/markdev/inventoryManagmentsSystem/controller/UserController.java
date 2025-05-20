package com.markdev.inventoryManagmentsSystem.controller;

import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.dto.UserDto;
import com.markdev.inventoryManagmentsSystem.entity.User;
import com.markdev.inventoryManagmentsSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUser(id,userDto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Response> getUserTransactions(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserTransaction(id));
    }
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentLoggedInUser());
    }

}
