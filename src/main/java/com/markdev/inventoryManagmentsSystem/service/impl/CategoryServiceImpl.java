package com.markdev.inventoryManagmentsSystem.service.impl;

import com.markdev.inventoryManagmentsSystem.dto.CategoryDto;
import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.entity.Category;
import com.markdev.inventoryManagmentsSystem.exceptions.NotFoundException;
import com.markdev.inventoryManagmentsSystem.repository.CategoryRepository;
import com.markdev.inventoryManagmentsSystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response createCategory(CategoryDto categoryDto) {
        Category categoryToSave = modelMapper.map(categoryDto,Category.class);
         categoryRepository.save(categoryToSave);
        return Response.builder()
                .status(200)
                .message("Category successfully created")
                .build();
    }

    @Override
     public Response getAllCategories() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<CategoryDto> categoriesDtos = modelMapper.map(categories, new TypeToken<List<CategoryDto>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .categories(categoriesDtos)
                .build();
    }

    @Override
    public Response getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Category id:"+id+" not found"));
        CategoryDto categoryDto = modelMapper.map(category,CategoryDto.class);

        return Response.builder()
                .status(200)
                .message("success")
                .category(categoryDto)
                .build();
    }

    @Override
    public Response updateCategory(Long id, CategoryDto categoryDto) {
        Category categoryToUpdate = categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Category id:"+id+" not found"));

        if(categoryDto.getName() != null) categoryToUpdate.setName(categoryDto.getName());
        categoryRepository.save(categoryToUpdate);

        return Response.builder()
                .status(200)
                .message("Category successfully updated")
                .build();
    }

    @Override
    public Response deleteCategory(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Category id:"+id+"  not found"));
        categoryRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Category deleted successfully")
                .build();
    }
}
