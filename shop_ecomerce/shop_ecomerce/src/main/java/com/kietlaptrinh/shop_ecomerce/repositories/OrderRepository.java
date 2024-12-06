package com.kietlaptrinh.shop_ecomerce.repositories;

import com.kietlaptrinh.shop_ecomerce.auth.entities.User;
import com.kietlaptrinh.shop_ecomerce.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
}
