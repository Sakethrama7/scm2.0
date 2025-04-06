package com.scm20.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm20.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{
   // extra methods db related operations
   // custom query methods
   // custom finder methods

   // These below methods are custom methods and implementation of these methods are automatically done by JPA.
   Optional<User> findByEmail(String email);
   Optional<User> findByEmailAndPassword(String email,String password);

   
}
