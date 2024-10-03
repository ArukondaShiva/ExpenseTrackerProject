package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategoryDTO;
import com.example.expensetracker.dto.UserDTO;
import com.example.expensetracker.entity.CategoryEntity;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.exceptions.ItemAlreadyExistsException;
import com.example.expensetracker.exceptions.ResourceNotFoundException;
import com.example.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final UserService userService;



    /**
     * This is for reading the categories from database
     * @return list
     */
    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> list = categoryRepository.findByUserId(userService.getLoggedInUser().getId());
        return list.stream().map(categoryEntity -> mapToDTO(categoryEntity)).collect(Collectors.toList());
    }



    /**
     * This is for creating the new category.
     * @param categoryDTO
     * @return CategoryDTO
     */
    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {

        boolean isCategoryPresent = categoryRepository.existsByNameAndUserId(categoryDTO.getName(),userService.getLoggedInUser().getId());

        if(isCategoryPresent){
            throw new ItemAlreadyExistsException("Category is already present for the name "+categoryDTO.getName());
        }

        CategoryEntity newCategory = mapToEntity(categoryDTO);
        newCategory = categoryRepository.save(newCategory);
        return mapToDTO(newCategory);

    }


    /**
     * This is for Deleting the Category from Database
     * @param categoryId
     */
    @Override
    public void deleteCategory(String categoryId) {
       Optional<CategoryEntity> optionalCategory = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(),categoryId);
       if(!optionalCategory.isPresent()){
           throw new ResourceNotFoundException("Category not found for the id : "+categoryId);
       }
       categoryRepository.delete(optionalCategory.get());
    }


    /**
     * Mapper method to convert CategoryDTO to CategoryEntity
     * @param categoryDTO
     * @return CategoryEntity
     */
    private CategoryEntity mapToEntity(CategoryDTO categoryDTO) {

        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .categoryIcon(categoryDTO.getCategoryIcon())
                .categoryId(UUID.randomUUID().toString())
                .user(userService.getLoggedInUser())
                .build();
    }


    /**
     * Mapper method to convert Category Entity to Category DTO
     * @param categoryEntity
     * @return CategoryDTO
     */
    private CategoryDTO mapToDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .categoryId(categoryEntity.getCategoryId())
                .name(categoryEntity.getName())
                .description(categoryEntity.getDescription())
                .categoryIcon(categoryEntity.getCategoryIcon())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .user(mapToDTO(categoryEntity.getUser()))
                .build();
    }


    /**
     * Mapper method to convert User entity to UserDTO
     * @param user
     * @return USerDTO
     */
    private UserDTO mapToDTO(User user) {

        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();

    }

}
