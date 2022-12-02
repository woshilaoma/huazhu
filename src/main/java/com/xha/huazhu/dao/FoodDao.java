package com.xha.huazhu.dao;

import com.xha.huazhu.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodDao extends JpaRepository<Food,Integer> {
}
