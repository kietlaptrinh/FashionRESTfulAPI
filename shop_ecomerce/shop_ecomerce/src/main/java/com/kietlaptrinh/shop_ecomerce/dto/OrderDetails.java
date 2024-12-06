package com.kietlaptrinh.shop_ecomerce.dto;

import com.kietlaptrinh.shop_ecomerce.entities.Address;
import com.kietlaptrinh.shop_ecomerce.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetails {
    private UUID id;
    private Date orderDate;
    private Address address;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private String shipmentNumber;
    private Date expectedDeliveryDate;
    private List<OrderItemDetails> orderItemList;


}
