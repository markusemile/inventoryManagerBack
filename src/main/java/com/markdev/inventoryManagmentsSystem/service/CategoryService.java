package com.markdev.inventoryManagmentsSystem.service;


import com.markdev.inventoryManagmentsSystem.dto.CategoryDto;
import com.markdev.inventoryManagmentsSystem.dto.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryDto);
    Response getAllCategories();
    Response getCategoryById(Long id);
    Response updateCategory(Long id,CategoryDto categoryDto);
    Response deleteCategory(Long id);

}
