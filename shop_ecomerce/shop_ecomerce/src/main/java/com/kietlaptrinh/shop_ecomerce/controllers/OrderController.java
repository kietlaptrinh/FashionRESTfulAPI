package com.kietlaptrinh.shop_ecomerce.controllers;

import com.kietlaptrinh.shop_ecomerce.auth.dto.OrderResponse;
import com.kietlaptrinh.shop_ecomerce.dto.OrderDetails;
import com.kietlaptrinh.shop_ecomerce.dto.OrderRequest;
import com.kietlaptrinh.shop_ecomerce.services.OrderService;
import com.kietlaptrinh.shop_ecomerce.services.PayPalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/order")
@CrossOrigin()
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    PayPalService payPalService;
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, Principal principal) throws Exception {
        OrderResponse orderResponse = orderService.createOrder(orderRequest, principal);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);

    }
    @PostMapping("/update-payment")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody Map<String,String> request){
        Map<String,String> response = orderService.updateStatus(request.get("paymentIntent"),request.get("status"));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderDetails>> getOrderByUser(Principal principal){
        List<OrderDetails> orders = orderService.getOrdersByUser(principal.getName());
        return new ResponseEntity<>(orders,HttpStatus.OK);

    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable UUID id, Principal principal){
        orderService.cancelOrder(id,principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //PAYPAL
    @RequestMapping(value = "/paypal/success", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> paypalSuccess(@RequestParam("paymentId") String paymentId,
                                           @RequestParam("PayerID") String payerId) {
        System.out.println("Received paymentId: " + paymentId);
        System.out.println("Received PayerID: " + payerId);
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            String orderId = payment.getTransactions().get(0).getDescription().split("#")[1];
            orderService.updateStatus(orderId, "COMPLETED");
            return new ResponseEntity<>(Map.of("status", "success"), HttpStatus.OK);
        } catch (PayPalRESTException e) {
            return new ResponseEntity<>(Map.of("status", "error", "message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/paypal/cancel")
    public ResponseEntity<?> paypalCancel() {
        return new ResponseEntity<>(Map.of("status", "cancelled"), HttpStatus.OK);
    }

}
