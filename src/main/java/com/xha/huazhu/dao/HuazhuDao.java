package com.xha.huazhu.dao;

import com.xha.huazhu.entity.Food;
import com.xha.huazhu.entity.Huazhu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface HuazhuDao extends JpaRepository<Huazhu, Integer> {

}

