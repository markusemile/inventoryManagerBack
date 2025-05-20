package com.markdev.inventoryManagmentsSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.markdev.inventoryManagmentsSystem.entity.Product;
import com.markdev.inventoryManagmentsSystem.enums.TransactionStatus;
import com.markdev.inventoryManagmentsSystem.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {

    private Long id;
    private Integer totalProducts;
    private BigDecimal totalPrice;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private String note;
    private LocalDateTime updateAt;
    private final LocalDateTime createAt = LocalDateTime.now();
    private UserDto user;
    private ProductDto product;
    private SupplierDto supplier;


}
