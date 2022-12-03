package com.xha.huazhu.dao;

import com.xha.huazhu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Integer> {
    int countByQq(long qq);

    User queryByQq(long qq);
}
