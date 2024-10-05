package com.example.expensetracker.repository;

import com.example.expensetracker.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * JPA Repository for Category Entity
 * @author Shiva Arukonda
 */
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long>{



    /**
     * finder method to retrieve the categories by user id
     * @param userId
     * @return list
     */
    List<CategoryEntity> findByUserId(Long userId);




    /**
     * finder method to fetch the category by user id and category id
     * @param id
     * @param categoryId
     * @return Optional<CategoryEntity>
     */
    Optional<CategoryEntity> findByUserIdAndCategoryId(Long id, String categoryId);



    /**
     * It checks whether category is present or not by user id and category name.
     * @param name
     * @param userId
     * @return boolean
     */
    boolean existsByNameAndUserId(String name,Long userId);


    /**
     * it retrives the category by name and user id
     * @param name
     * @param userId
     * @return Optional<CategoryEntity>
     */
    Optional<CategoryEntity> findByNameAndUserId(String name,Long userId);

}
