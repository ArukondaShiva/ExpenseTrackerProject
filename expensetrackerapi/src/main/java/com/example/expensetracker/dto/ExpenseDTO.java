package com.example.expensetracker.dto;

import com.example.expensetracker.io.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDTO {
    private String expenseId;
    private String name;
    private String description;
    private BigDecimal amount;
    private Date date;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private CategoryDTO categoryDTO;
    private UserDTO userDTO;
    private String categoryId;
}
