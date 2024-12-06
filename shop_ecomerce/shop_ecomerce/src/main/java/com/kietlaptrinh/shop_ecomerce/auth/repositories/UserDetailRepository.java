package com.kietlaptrinh.shop_ecomerce.auth.repositories;

import com.kietlaptrinh.shop_ecomerce.auth.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);

}
