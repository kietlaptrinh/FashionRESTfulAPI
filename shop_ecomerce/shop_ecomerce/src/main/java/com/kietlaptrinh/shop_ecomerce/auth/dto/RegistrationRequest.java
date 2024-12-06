package com.kietlaptrinh.shop_ecomerce.auth.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private CharSequence password;
    private String phoneNumber;



}
