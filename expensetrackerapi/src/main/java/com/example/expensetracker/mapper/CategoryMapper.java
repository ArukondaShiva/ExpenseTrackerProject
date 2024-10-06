package com.example.expensetracker.mapper;


import com.example.expensetracker.dto.CategoryDTO;
import com.example.expensetracker.entity.CategoryEntity;
import com.example.expensetracker.io.CategoryRequest;
import com.example.expensetracker.io.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryEntity mapToCategoryEntity(CategoryDTO categoryDTO);

    CategoryDTO mapToCategoryDTO(CategoryEntity entity);

    @Mapping(target = "categoryIcon",source = "icon")
    CategoryDTO mapToCategoryDTO(CategoryRequest request);

    CategoryResponse mapToCategoryResponse(CategoryDTO categoryDTO);

}
