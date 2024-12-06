import React, { useEffect } from "react";

const PayPalButton1 = ({ totalAmount, onSuccess }) => {
  useEffect(() => {
    if (window.paypal) {
      window.paypal
        .Buttons({
          createOrder: (data, actions) => {
            return actions.order.create({
              purchase_units: [
                {
                  amount: {
                    value: totalAmount,
                  },
                },
              ],
            });
          },
          onApprove: async (data, actions) => {
            const order = await actions.order.capture();
            console.log("Order Successful: ", order);
            onSuccess(order);
          },
          onError: (err) => {
            console.error("PayPal Checkout Error: ", err);
          },
        })
        .render("#paypal-button-container");
    }
  }, [totalAmount, onSuccess]);

  return <div id="paypal-button-container"></div>;
};

export default PayPalButton1;
