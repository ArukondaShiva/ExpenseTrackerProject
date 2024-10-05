package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategoryDTO;
import com.example.expensetracker.dto.ExpenseDTO;
import com.example.expensetracker.entity.CategoryEntity;
import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.exceptions.ResourceNotFoundException;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService{

    private final ExpenseRepository expenseRepository;

    private final UserService userService;

    private final CategoryRepository categoryRepository;


    @Override
    public List<ExpenseDTO> getAllExpenses(Pageable page) {

        List<Expense> listOfExpense = expenseRepository.findByUserId(userService.getLoggedInUser().getId(),page).toList();
        return listOfExpense.stream().map(expense -> mapToDTO(expense)).collect(Collectors.toList());

    }

    @Override
    public ExpenseDTO getExpenseById(String expenseId) {
        Expense existingExpense = getExpenseEntity(expenseId);
        return mapToDTO(existingExpense);
    }


    private Expense getExpenseEntity(String expenseId){
        Optional<Expense> expense = expenseRepository.findByUserIdAndExpenseId(userService.getLoggedInUser().getId(), expenseId);
        if(!expense.isPresent()){
            throw new ResourceNotFoundException("Expense is not found for the id : "+expenseId);
        }

        return expense.get();
    }

    @Override
    public void deleteExpenseById(String expenseId) {
        Expense expense = getExpenseEntity(expenseId);
        //expenseRepository.deleteById(id);
        expenseRepository.delete(expense);
    }

    @Override
    public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) {

        // check the existence of category.
        Optional<CategoryEntity> optionalCategory = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), expenseDTO.getCategoryId());

        if(!optionalCategory.isPresent()){
            throw new ResourceNotFoundException("Category not found for the id "+expenseDTO.getCategoryId());
        }

        expenseDTO.setExpenseId(UUID.randomUUID().toString());

        // map to entity object
        Expense newExpense = mapToEntity(expenseDTO);

        // save to the DB
        newExpense.setCategory(optionalCategory.get());
        newExpense.setUser(userService.getLoggedInUser());
        newExpense = expenseRepository.save(newExpense);

        // map to response object
        return mapToDTO(newExpense);
    }

    private ExpenseDTO mapToDTO(Expense newExpense) {

        return ExpenseDTO.builder()
                .expenseId(newExpense.getExpenseId())
                .name(newExpense.getName())
                .description(newExpense.getDescription())
                .amount(newExpense.getAmount())
                .date(newExpense.getDate())
                .createdAt(newExpense.getCreatedAt())
                .updatedAt(newExpense.getUpdatedAt())
                .categoryDTO(mapToCategoryDTO(newExpense.getCategory()))
                .build();

    }

    private CategoryDTO mapToCategoryDTO(CategoryEntity category){

        return CategoryDTO.builder()
                .name(category.getName())
                .categoryId(category.getCategoryId())
                .build();
    }

    private Expense mapToEntity(ExpenseDTO expenseDTO) {

        return Expense.builder()
                .expenseId(expenseDTO.getExpenseId())
                .name(expenseDTO.getName())
                .description(expenseDTO.getDescription())
                .date(expenseDTO.getDate())
                .amount(expenseDTO.getAmount())
                .build();
    }

    @Override
    public ExpenseDTO updateExpenseDetails(String expenseId, ExpenseDTO expenseDTO) {

        Expense existingExpense = getExpenseEntity(expenseId);

        if(expenseDTO.getCategoryId() != null){
           Optional<CategoryEntity> optionalCategory = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), expenseDTO.getCategoryId());
           if(!optionalCategory.isPresent()){
               throw new ResourceNotFoundException("Category not found for the id "+expenseDTO.getCategoryId());
           }
           existingExpense.setCategory(optionalCategory.get());
        }

        existingExpense.setName(expenseDTO.getName()!=null ? expenseDTO.getName() : existingExpense.getName());
        existingExpense.setDescription(expenseDTO.getDescription()!=null ? expenseDTO.getDescription() : existingExpense.getDescription());
        existingExpense.setDate(expenseDTO.getDate()!=null ? expenseDTO.getDate() : existingExpense.getDate());
        existingExpense.setAmount(expenseDTO.getAmount()!=null ? expenseDTO.getAmount() : existingExpense.getAmount());

        existingExpense = expenseRepository.save(existingExpense);

        return mapToDTO(existingExpense);
    }

    @Override
    public List<ExpenseDTO> readByCategory(String category, Pageable page) {

        Optional<CategoryEntity> optionalCategory = categoryRepository.findByNameAndUserId(category,userService.getLoggedInUser().getId());

        if(!optionalCategory.isPresent()){
            throw new ResourceNotFoundException("Category not found for the name "+category);
        }

        List<Expense> list = expenseRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(),optionalCategory.get().getId(),page).toList();
        return list.stream().map(expense -> mapToDTO(expense)).collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> readByName(String name, Pageable page) {
        List<Expense> list = expenseRepository.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(),name,page).toList();
        return list.stream().map(expense -> mapToDTO(expense)).collect(Collectors.toList());
    }


    @Override
    public List<ExpenseDTO> readByDate(Date startDate, Date endDate, Pageable page){

        if(startDate == null){
            startDate = new Date(0);
        }

        if(endDate == null){
            endDate = new Date(System.currentTimeMillis());
        }

        List<Expense> list = expenseRepository.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(),startDate, endDate, page).toList();

        return list.stream().map(expense -> mapToDTO(expense)).collect(Collectors.toList());
    }


}
