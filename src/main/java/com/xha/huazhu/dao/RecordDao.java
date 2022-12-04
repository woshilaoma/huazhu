package com.xha.huazhu.dao;

import com.xha.huazhu.entity.Food;
import com.xha.huazhu.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RecordDao extends JpaRepository<Record, Integer> {

    @Query(value = "select t.id,t.create_time,t.food_id,t.huazhu_id,t.user_id user_id ,sum(t.food_count) as food_count from record t group by t.user_id order by food_count desc limit  5", nativeQuery = true)
    List<Record> findRank(int i);
}
