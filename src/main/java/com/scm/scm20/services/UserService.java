package com.scm.scm20.services;

import java.util.List;
import java.util.Optional;

import com.scm.scm20.entity.User;

public interface UserService {
    User saveUser(User user);
    User getUserById(String id);
    Optional<User> updatUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    List<User> getAllUsers();
    User getUserByEmail(String email);
    // add more methods here realated to service logic
}
