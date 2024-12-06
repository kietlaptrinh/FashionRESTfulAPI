package com.kietlaptrinh.shop_ecomerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private UUID id;
    private String name;
    private String code;
    private String description;

    //thay doi categoryTypes sang categoryTypeList
    private List<CategoryTypeDto> categoryTypes;
}
