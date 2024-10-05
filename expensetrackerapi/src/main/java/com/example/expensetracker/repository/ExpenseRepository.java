package com.example.expensetracker.repository;


import com.example.expensetracker.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {

     Page<Expense> findByUserIdAndCategory(Long userId,String category, Pageable page);

     Page<Expense> findByUserIdAndCategoryId(Long userId,Long categoryId, Pageable page);

     Page<Expense> findByUserIdAndNameContaining(Long userId,String keyword,Pageable page);

     Page<Expense> findByUserIdAndDateBetween(Long userId,Date startDate,Date endDate,Pageable page);

     Page<Expense> findByUserId(Long userId, Pageable pageable);

     Optional<Expense> findByUserIdAndExpenseId(Long userId, String expenseId);


}
