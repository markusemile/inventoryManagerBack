package com.markdev.inventoryManagmentsSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
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
public class ProductDto {
    private Long id;
    private Long categoryId;
    private Long supplierId;
    private String name;
    private String sku;
    private BigDecimal price;
    private Integer stockQuantity;
    @Lob
    @Column(length = 65535)
    private String description;
    private String imageUrl;
    private LocalDateTime expiryDate;
    private LocalDateTime updateAt;
    private LocalDateTime createAt;
    private CategoryDto category;


}
