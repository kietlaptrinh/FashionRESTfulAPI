package com.kietlaptrinh.shop_ecomerce.controllers;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private APIContext apiContext;

    @PostMapping("/create-payment")
    public String createPayment(@RequestParam double total, @RequestParam String currency) {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription("Payment description");
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(
               "http://localhost:3000/cancel"
        );
        redirectUrls.setReturnUrl(
                "http://localhost:3000/success"
        );
        payment.setRedirectUrls(redirectUrls);

        try {
            Payment createdPayment = payment.create(apiContext);
            for (Links link : createdPayment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return link.getHref(); // Trả về URL để frontend redirect
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "Error during payment creation";
    }

    @PostMapping("/execute-payment")
    public String executePayment(@RequestParam String paymentId, @RequestParam String payerId) {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            Payment executedPayment = payment.execute(apiContext, paymentExecution);
            return "Payment executed successfully";
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "Error during payment execution";
    }
}

