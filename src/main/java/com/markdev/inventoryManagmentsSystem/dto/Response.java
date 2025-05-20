package com.markdev.inventoryManagmentsSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.markdev.inventoryManagmentsSystem.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    // generic
    private int status;
    private String message;
    //for login
    private String token;
    private UserRole role;
    private String expirationTime;
    //for pagination
    private Integer totalPages;
    private Long totalElement;
    private Integer currentPage;

    //data output options
    private UserDto user;
    private List<UserDto> users;

    private SupplierDto supplier;
    private List<SupplierDto> suppliers;

    private CategoryDto category;
    private List<CategoryDto> categories;

    private ProductDto product;
    private List<ProductDto> products;

    private TransactionDto transaction;
    private List<TransactionDto> transactions;

    private LocalDateTime timestamp = LocalDateTime.now();



}
