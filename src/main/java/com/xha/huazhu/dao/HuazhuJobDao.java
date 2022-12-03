package com.xha.huazhu.dao;

import com.xha.huazhu.entity.HuazhuJob;
import com.xha.huazhu.entity.HuazhuStomach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HuazhuJobDao extends JpaRepository<HuazhuJob, Integer> {


    List<HuazhuJob> findAllByOrderByEventTimeAsc();
}

