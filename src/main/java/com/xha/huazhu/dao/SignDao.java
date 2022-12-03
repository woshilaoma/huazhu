package com.xha.huazhu.dao;

import com.xha.huazhu.entity.Sign;
import com.xha.huazhu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignDao extends JpaRepository<Sign,Integer> {
    int countByUserIdAndDay(Integer id, int day);
}
