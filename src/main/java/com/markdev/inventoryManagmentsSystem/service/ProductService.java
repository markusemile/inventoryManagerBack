package com.markdev.inventoryManagmentsSystem.service;


import com.markdev.inventoryManagmentsSystem.dto.CategoryDto;
import com.markdev.inventoryManagmentsSystem.dto.ProductDto;
import com.markdev.inventoryManagmentsSystem.dto.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Response createProduct(ProductDto productDto, MultipartFile filename);
    Response getAllProducts(Pageable pageable);
    Response getProductById(Long id);
    Response updateProduct(ProductDto productDto, MultipartFile filename);
    Response deleteProduct(Long id);

    Response searchProductPaginated(String searchText, Pageable pageable);

}
