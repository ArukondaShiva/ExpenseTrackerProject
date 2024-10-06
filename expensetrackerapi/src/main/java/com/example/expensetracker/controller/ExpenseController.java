package com.example.expensetracker.controller;


import com.example.expensetracker.dto.CategoryDTO;
import com.example.expensetracker.dto.ExpenseDTO;
import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.io.CategoryResponse;
import com.example.expensetracker.io.ExpenseRequest;
import com.example.expensetracker.io.ExpenseResponse;
import com.example.expensetracker.mapper.ExpenseMapper;
import com.example.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseMapper expenseMapper;



    @GetMapping("/expenses")
    public List<ExpenseResponse> getAllExpenses(Pageable page){
         List<ExpenseDTO> listOfExpenses = expenseService.getAllExpenses(page);
         return listOfExpenses.stream().map(expenseDTO -> expenseMapper.mapToExpenseResponse(expenseDTO)).collect(Collectors.toList());
    }


    @GetMapping("/expenses/{expenseId}")
    public ExpenseResponse getExpenseById(@PathVariable String expenseId){
        ExpenseDTO expenseDTO = expenseService.getExpenseById(expenseId);
        return expenseMapper.mapToExpenseResponse(expenseDTO);
    }


    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/expenses")
    public void deleteExpenseById(@RequestParam String expenseId){
         expenseService.deleteExpenseById(expenseId);
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/expenses")
    public ExpenseResponse saveExpenseDetails(@Valid @RequestBody ExpenseRequest expenseRequest){
        ExpenseDTO expenseDTO =  expenseMapper.mapToExpenseDTO(expenseRequest);
        expenseDTO = expenseService.saveExpenseDetails(expenseDTO);
        return expenseMapper.mapToExpenseResponse(expenseDTO);
    }


    @PutMapping("/expenses/{expenseId}")
    public ExpenseResponse updateExpenseDetails(@RequestBody ExpenseRequest expenseRequest,@PathVariable String expenseId){
         ExpenseDTO updatedExpense = expenseMapper.mapToExpenseDTO(expenseRequest);
         updatedExpense = expenseService.updateExpenseDetails(expenseId,updatedExpense);
         return expenseMapper.mapToExpenseResponse(updatedExpense);
    }


    @GetMapping("/expenses/category")
    public List<ExpenseResponse> getExpensesByCategory(@RequestParam String category, Pageable page){
        List<ExpenseDTO> list = expenseService.readByCategory(category, page);

        return list.stream().map(expenseDTO -> expenseMapper.mapToExpenseResponse(expenseDTO)).collect(Collectors.toList());
    }


    @GetMapping("/expenses/name")
    public List<ExpenseResponse> getAllExpensesByName(@RequestParam String keyword, Pageable page){
        List<ExpenseDTO> list = expenseService.readByName(keyword,page);
        return list.stream().map(expenseDTO -> expenseMapper.mapToExpenseResponse(expenseDTO)).collect(Collectors.toList());
    }


    @GetMapping("/expenses/date")
    public List<ExpenseResponse> getExpensesByDates(@RequestParam(required = false) Date startDate,
                                            @RequestParam(required = false) Date endDate,
                                            Pageable page){

        List<ExpenseDTO> list = expenseService.readByDate(startDate, endDate, page);

        return list.stream().map(expenseDTO -> expenseMapper.mapToExpenseResponse(expenseDTO)).collect(Collectors.toList());
    }

}