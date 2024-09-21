package com.example.expensetracker.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpenseController {


    @GetMapping("/expenses")
    public String getAllExpenses(){
       return "List Of Expenses";
    }


}
