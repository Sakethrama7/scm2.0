package com.scm20.services;

import java.util.List;
import java.util.Optional;

import com.scm20.entities.User;

public interface UserService {
   User saveUser(User user);
   Optional<User> getUserById(String id);
   Optional<User> updateUser(User user);
   void deleteUser(String id);
   boolean isUserExits(String userId);
   boolean isUserExitByEmail(String email);
   List<User> getAllUsers();
    
   User getUserByEmail(String email);
   // add more methods here related user service(logic)
}
