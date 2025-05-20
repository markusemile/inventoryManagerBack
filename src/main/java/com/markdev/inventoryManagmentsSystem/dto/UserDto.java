package com.markdev.inventoryManagmentsSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.markdev.inventoryManagmentsSystem.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String phoneNumber;
    private UserRole role;
    private List<TransactionDto> transactions;
    private LocalDateTime createAt;

}
