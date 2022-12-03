package com.xha.huazhu.dao;

import com.xha.huazhu.entity.Huazhu;
import com.xha.huazhu.entity.HuazhuStomach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface HuazhuStomachDao extends JpaRepository<HuazhuStomach, Integer> {


    @Modifying
    @Transactional
    @Query("delete from HuazhuStomach where foodId = ?1")
    void deleteByFoodId(Integer foodId);
}

