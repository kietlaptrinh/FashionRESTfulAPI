package com.kietlaptrinh.shop_ecomerce.repositories;

import com.kietlaptrinh.shop_ecomerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
