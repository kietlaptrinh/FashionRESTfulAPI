package com.kietlaptrinh.shop_ecomerce.repositories;

import com.kietlaptrinh.shop_ecomerce.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
