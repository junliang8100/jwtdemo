package com.offcn.dao;

import com.offcn.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<User,Long> {

    public User findByUsername(String username);
}
