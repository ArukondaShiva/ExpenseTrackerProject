package com.example.expensetracker.controller;


import com.example.expensetracker.dto.CategoryDTO;
import com.example.expensetracker.io.CategoryRequest;
import com.example.expensetracker.io.CategoryResponse;
import com.example.expensetracker.mapper.CategoryMapper;
import com.example.expensetracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * This Controller is for managing the categories.
 * @author Shiva Arukonda
 * */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;


    /***
     *  API for creating the category
     * @param categoryRequest
     * @return CategoryResponse
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryResponse createCategory(@RequestBody CategoryRequest categoryRequest){
        CategoryDTO categoryDTO = categoryMapper.mapToCategoryDTO(categoryRequest);
        categoryDTO = categoryService.saveCategory(categoryDTO);
        return categoryMapper.mapToCategoryResponse(categoryDTO);
    }


    /**
     * API for reading the categories
     * @return list
     */
    @GetMapping
    public List<CategoryResponse> readCategories(){
        List<CategoryDTO> listOfCategories = categoryService.getAllCategories();
        return listOfCategories.stream().map(categoryDTO -> categoryMapper.mapToCategoryResponse(categoryDTO)).collect(Collectors.toList());
    }



    /***
     * API for deleting the categories
     * @param categoryId
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable String categoryId){
        categoryService.deleteCategory(categoryId);
    }



}
