package com.example.expensetracker.service;

import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.exceptions.ResourceNotFoundException;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


@Service
public class ExpenseServiceImpl implements ExpenseService{


    @Autowired
    private ExpenseRepository expenseRepository;


    @Autowired
    private UserService userService;


    @Override
    public Page<Expense> getAllExpenses(Pageable page) {
        return expenseRepository.findByUserId(userService.getLoggedInUser().getId(),page);
    }

    @Override
    public Expense getExpenseById(Long id) {
        Optional<Expense> expense = expenseRepository.findByUserIdAndId(userService.getLoggedInUser().getId(),id);

        if(expense.isPresent()){
            return expense.get();
        }
        throw new ResourceNotFoundException("Expense is not found for the id : "+id);
    }

    @Override
    public void deleteExpenseById(Long id) {
        Expense expense = getExpenseById(id);
        //expenseRepository.deleteById(id);
        expenseRepository.delete(expense);
    }

    @Override
    public Expense saveExpenseDetails(Expense expense) {
        expense.setUser(userService.getLoggedInUser());
        return expenseRepository.save(expense);
    }

    @Override
    public Expense updateExpenseDetails(Long id, Expense expense) {

        Expense existingExpense = getExpenseById(id);

        existingExpense.setName(expense.getName()!=null ? expense.getName() : existingExpense.getName());
        existingExpense.setDescription(expense.getDescription()!=null ? expense.getDescription() : existingExpense.getDescription());
        existingExpense.setCategory(expense.getCategory()!=null ? expense.getCategory() : existingExpense.getCategory());
        existingExpense.setDate(expense.getDate()!=null ? expense.getDate() : existingExpense.getDate());
        existingExpense.setAmount(expense.getAmount()!=null ? expense.getAmount() : existingExpense.getAmount());

        return expenseRepository.save(existingExpense);
    }

    @Override
    public List<Expense> readByCategory(String category, Pageable page) {
        return expenseRepository.findByUserIdAndCategory(userService.getLoggedInUser().getId(),category,page).toList();
    }

    @Override
    public List<Expense> readByName(String name, Pageable page) {
        return expenseRepository.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(),name,page).toList();
    }


    @Override
    public List<Expense> readByDate(Date startDate, Date endDate, Pageable page){

        if(startDate == null){
            startDate = new Date(0);
        }

        if(endDate == null){
            endDate = new Date(System.currentTimeMillis());
        }

        return expenseRepository.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(),startDate, endDate, page).toList();
    }


}
