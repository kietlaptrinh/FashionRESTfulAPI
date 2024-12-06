package com.kietlaptrinh.shop_ecomerce.auth.repositories;

import com.kietlaptrinh.shop_ecomerce.auth.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, UUID> {

    Authority findByRoleCode(String user);
}
