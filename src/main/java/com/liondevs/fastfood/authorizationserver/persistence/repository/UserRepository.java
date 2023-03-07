package com.liondevs.fastfood.authorizationserver.persistence.repository;

import com.liondevs.fastfood.authorizationserver.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

   public  Optional<User> findByEmail(String email);

}
