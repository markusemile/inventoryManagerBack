package com.markdev.inventoryManagmentsSystem.controller;

import com.markdev.inventoryManagmentsSystem.dto.ProductDto;
import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllProduct(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ){
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").descending());
        return ResponseEntity.ok(productService.searchProductPaginated(query,pageable));
        //return ResponseEntity.ok(productService.getAllProducts());
    }




    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewProduct(
           @RequestParam("imageFile") MultipartFile imageFile,
           @RequestParam("name") String name,
           @RequestParam("sku") String sku ,
           @RequestParam("price") BigDecimal price,
           @RequestParam("stockQuantity") Integer stockQuantity,
           @RequestParam("categoryId") Long categoryId,
           @RequestParam(value="description", required = false) String description
           ){
        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setSku(sku);
        productDto.setPrice(price);
        productDto.setStockQuantity(stockQuantity);
        productDto.setCategoryId(categoryId);
        productDto.setDescription(description);

        return ResponseEntity.ok(productService.createProduct(productDto,imageFile));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(

            @RequestParam(value="imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value="name", required = false) String name,
            @RequestParam(value="sku", required = false) String sku ,
            @RequestParam(value="price", required = false) BigDecimal price,
            @RequestParam(value="stockQuantity", required = false) Integer stockQuantity,
            @RequestParam(value="productId") Long productId,
            @RequestParam(value="categoryId") Long categoryId,
            @RequestParam(value="description", required = false) String description

    ){


        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setSku(sku);
        productDto.setPrice(price);
        productDto.setStockQuantity(stockQuantity);
        productDto.setCategoryId(categoryId);
        productDto.setId(productId);
        productDto.setDescription(description);
        productDto.setUpdateAt(LocalDateTime.now());

        return ResponseEntity.ok(productService.updateProduct(productDto,imageFile));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id){

        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getSupplierById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }


}
