package com.markdev.inventoryManagmentsSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {
    @Positive(message="Product id is required")
    private Long productId;
    @Positive(message="Quantity is required")
    @Min(value = 1,message = "Quantity must be at least 1")
    private Integer quantity;
    @Positive(message="Supplier id is required")
    private Long supplierId;
    private String description;

}
