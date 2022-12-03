package com.xha.huazhu.dao;

import com.xha.huazhu.entity.Food;
import com.xha.huazhu.entity.UserPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserPackageDao extends JpaRepository<UserPackage,Integer> {

    UserPackage getByUserIdAndFoodId(Integer userId, Integer foodId);

    List<UserPackage> getByUserId(Integer userId);

    @Modifying
    @Transactional
    @Query("delete from UserPackage where foodId = ?1")
    void deleteByFoodId(Integer foodId);
}
