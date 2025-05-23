package com.scm.scm20.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.scm20.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
//extra methods for DB intarcation

Optional<User> findByEmail(String email);
Optional<User> findByEmailAndPassword(String email, String password);
    
} 
