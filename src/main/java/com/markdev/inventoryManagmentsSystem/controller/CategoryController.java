package com.markdev.inventoryManagmentsSystem.controller;

import com.markdev.inventoryManagmentsSystem.dto.CategoryDto;
import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/category")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/new")
    public ResponseEntity<Response> addNewCategory(@RequestBody @Valid CategoryDto categoryToSave){
        return ResponseEntity.ok(categoryService.createCategory(categoryToSave));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryToUpdate){
        return ResponseEntity.ok(categoryService.updateCategory(id,categoryToUpdate));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }


}
