package com.kietlaptrinh.shop_ecomerce.auth.dto;
//
////import jakarta.mail.Address;
import com.kietlaptrinh.shop_ecomerce.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Object authorityList;
    private List<Address> addressList;
}