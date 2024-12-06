package com.kietlaptrinh.shop_ecomerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String name;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
}
