package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategoryDTO;
import com.example.expensetracker.dto.UserDTO;
import com.example.expensetracker.entity.CategoryEntity;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.exceptions.ItemAlreadyExistsException;
import com.example.expensetracker.exceptions.ResourceNotFoundException;
import com.example.expensetracker.mapper.CategoryMapper;
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
    private final CategoryMapper categoryMapper;

    /**
     * This is for reading the categories from database
     * @return list
     */
    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> list = categoryRepository.findByUserId(userService.getLoggedInUser().getId());
        return list.stream().map(categoryEntity -> categoryMapper.mapToCategoryDTO(categoryEntity)).collect(Collectors.toList());
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

        CategoryEntity newCategory = categoryMapper.mapToCategoryEntity(categoryDTO);
        newCategory.setCategoryId(UUID.randomUUID().toString());
        newCategory.setUser(userService.getLoggedInUser());
        newCategory = categoryRepository.save(newCategory);
        return categoryMapper.mapToCategoryDTO(newCategory);

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
     * Mapper method to convert User entity to UserDTO
     * @param user
     * @return USerDTO
     */
    private UserDTO mapToUserDTO(User user) {

        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();

    }

}
