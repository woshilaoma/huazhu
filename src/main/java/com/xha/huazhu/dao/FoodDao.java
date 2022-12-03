package com.xha.huazhu.dao;

import com.xha.huazhu.entity.Food;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FoodDao extends JpaRepository<Food,Integer> {
    int countByFoodName(String foodName);

    @Modifying
    @Transactional
    @Query("delete from Food where foodName = ?1")
    void deleteByFoodName(String foodName);

    Food getByFoodName(String foodName);
}
