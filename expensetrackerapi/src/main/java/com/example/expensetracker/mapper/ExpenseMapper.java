package com.example.expensetracker.mapper;


import com.example.expensetracker.dto.ExpenseDTO;
import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.io.ExpenseRequest;
import com.example.expensetracker.io.ExpenseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    @Mapping(target = "category", source = "expenseDTO.categoryDTO")
    ExpenseResponse mapToExpenseResponse(ExpenseDTO expenseDTO);

    ExpenseDTO mapToExpenseDTO(ExpenseRequest request);

    Expense mapToExpenseEntity(ExpenseDTO expenseDTO);

    @Mapping(target = "categoryDTO", source = "expense.category")
    ExpenseDTO mapToExpenseDtO(Expense expense);


}
