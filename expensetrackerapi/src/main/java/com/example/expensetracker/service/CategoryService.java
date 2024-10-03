package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategoryDTO;

import java.util.List;


/**
 * Service Interface for managing the categories
 * @author Shiva Arukonda
 */
public interface CategoryService {


    /**
     * This is for reading the categories from database
     * @return list
     */
    List<CategoryDTO> getAllCategories();


    /**
     * This is for creating the new category.
     * @param categoryDTO
     * @return CategoryDTO
     */
    CategoryDTO saveCategory(CategoryDTO categoryDTO);


    /**
     * This is for Deleting the Category from Database
     * @param categoryId
     */
    void deleteCategory(String categoryId);

}
