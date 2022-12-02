package com.xha.huazhu.service.impl;

import com.xha.huazhu.dao.FoodDao;
import com.xha.huazhu.entity.Food;
import com.xha.huazhu.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodDao foodDao;
    @Override
    public boolean addFood(String foodName) {
        Food food = new Food();
        food.setFoodName(foodName);
        return foodDao.save(food).getId()>0;
    }
}
