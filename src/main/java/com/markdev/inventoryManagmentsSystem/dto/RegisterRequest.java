package com.markdev.inventoryManagmentsSystem.dto;

import com.markdev.inventoryManagmentsSystem.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Names is required")
     private String name;
    @NotBlank(message = "Email is required")
     private String email;
    @NotBlank(message = "Password is required")
     private String password;
    @NotBlank(message = "Phone number is required")
     private String phoneNumber;
     private UserRole role;

}
