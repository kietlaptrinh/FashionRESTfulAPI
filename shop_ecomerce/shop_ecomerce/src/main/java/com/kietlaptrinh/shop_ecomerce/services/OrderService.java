package com.kietlaptrinh.shop_ecomerce.services;

import com.kietlaptrinh.shop_ecomerce.dto.OrderDetails;
import com.kietlaptrinh.shop_ecomerce.dto.OrderItemDetails;
import com.stripe.model.PaymentIntent;
import com.kietlaptrinh.shop_ecomerce.auth.dto.OrderResponse;
import com.kietlaptrinh.shop_ecomerce.auth.entities.User;
import com.kietlaptrinh.shop_ecomerce.entities.Order;
import com.kietlaptrinh.shop_ecomerce.dto.OrderRequest;
import com.kietlaptrinh.shop_ecomerce.entities.*;
import com.kietlaptrinh.shop_ecomerce.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private  UserDetailsService userDetailsService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    ProductService productService;

@Autowired
PayPalService payPalService;
    @Autowired
    PaymentIntentService paymentIntentService;
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, Principal principal
                                     ) throws Exception{
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Address address = user.getAddressList().stream().filter(address1
        -> orderRequest.getAddressId().equals(address1.getId())).findFirst().orElseThrow(BadRequestException::new);
        // Build the order
        Order order= Order.builder()

                .user(user)
                .address(address)
                .totalAmount(orderRequest.getTotalAmount())
                .orderDate(orderRequest.getOrderDate())
                .discount(orderRequest.getDiscount())
                .expectedDeliveryDate(orderRequest.getExpectedDeliveryDate())
                .paymentMethod(orderRequest.getPaymentMethod())
                .orderStatus(OrderStatus.PENDING)
                .build();
        // Map order items from the request to actual order items
        List<OrderItem> orderItems = orderRequest.getOrderItemRequests().stream().map(orderItemRequest -> {
            try {
                Product product= productService.fetchProductById(orderItemRequest.getProductId());
                OrderItem orderItem= OrderItem.builder()
                        .product(product)
                        .productVariantId(orderItemRequest.getProductVariantId())
                        .quantity(orderItemRequest.getQuantity())
                        .order(order)
                        .build();
                return orderItem;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
        // Set the order items in the order
        order.setOrderItemList(orderItems);
        Payment payment=new Payment();
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(new Date());
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);

        // Create a response object for the created order
        OrderResponse orderResponse = OrderResponse.builder()
                .paymentMethod(orderRequest.getPaymentMethod())
                .orderId(savedOrder.getId())
                .build();

        //PAYPAL
        if ("PAYPAL".equalsIgnoreCase(orderRequest.getPaymentMethod())) {
          com.paypal.api.payments.Payment payment2 = payPalService.createPayment(
                    orderRequest.getTotalAmount(),
                    "USD",
                    "paypal",
                    "sale",
                    "Order #" + savedOrder.getId(),
                    "http://localhost:8082/api/order/paypal/cancel",
                    "http://localhost:8082/api/order/paypal/success"
            );

            // Get the approval link for PayPal
            String approvalLink = payment2.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Approval URL not found"))
                    .getHref();
            orderResponse.setCredentials(Map.of("approvalLink", approvalLink));
        }

        //Phuong thuc thanh toan Stripe
        if(Objects.equals(orderRequest.getPaymentMethod(), "CARD")){
            orderResponse.setCredentials(paymentIntentService.createPaymentIntent(order));
        }

        return orderResponse;

    }

    public Map<String, String> updateStatus(String paymentIntentId, String status) {
        try{
            PaymentIntent paymentIntent= PaymentIntent.retrieve(paymentIntentId);
            if (paymentIntent != null && paymentIntent.getStatus().equals("succeeded")) {
                String orderId = paymentIntent.getMetadata().get("orderId") ;
                Order order= orderRepository.findById(UUID.fromString(orderId)).orElseThrow(BadRequestException::new);
                Payment payment = order.getPayment();
                payment.setPaymentStatus(PaymentStatus.COMPLETED);
                payment.setPaymentMethod(paymentIntent.getPaymentMethod());
                order.setPaymentMethod(paymentIntent.getPaymentMethod());
                order.setOrderStatus(OrderStatus.IN_PROGRESS);
                order.setPayment(payment);
                Order savedOrder = orderRepository.save(order);
                Map<String,String> map = new HashMap<>();
                map.put("orderId", String.valueOf(savedOrder.getId()));
                return map;
            }
            else{
                throw new IllegalArgumentException("PaymentIntent not found or missing metadata");
            }
        }
        catch (Exception e){
            throw new IllegalArgumentException("PaymentIntent not found or missing metadata");
        }
    }

    public List<OrderDetails> getOrdersByUser(String name) {
        User user = (User) userDetailsService.loadUserByUsername(name);
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(order -> {
            return OrderDetails.builder()
                    .id(order.getId())
                    .orderDate(order.getOrderDate())
                    .orderStatus(order.getOrderStatus())
                    .shipmentNumber(order.getShipmentTrackingNumber())
                    .address(order.getAddress())
                    .totalAmount(order.getTotalAmount())
                    .orderItemList(getItemDetails(order.getOrderItemList()))
                    .expectedDeliveryDate(order.getExpectedDeliveryDate())
                    .build();
        }).toList();
    }

    private List<OrderItemDetails> getItemDetails(List<OrderItem> orderItemList) {
        return orderItemList.stream().map(orderItem -> {
            return OrderItemDetails.builder()
                    .id(orderItem.getId())
                    .itemPrice(orderItem.getItemPrice())
                    .product(orderItem.getProduct())
                    .productVariantId(orderItem.getProductVariantId())
                    .quantity(orderItem.getQuantity())
                    .build();
        }).toList();
    }

    public void cancelOrder(UUID id, Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Order order = orderRepository.findById(id).get();
        if(null != order && order.getUser().getId().equals(user.getId())){
            order.setOrderStatus(OrderStatus.CANCELLED);
            //logic refund
            orderRepository.save(order);
        }
        else{
            new RuntimeException("Invalid request");
        }
    }
}
