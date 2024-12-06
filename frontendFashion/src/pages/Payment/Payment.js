import { Elements } from "@stripe/react-stripe-js";
import React, { useEffect, useState } from "react";
import CheckOutPayment from "./CheckOutPayment";
import { loadStripe } from "@stripe/stripe-js";
import { useDispatch, useSelector } from "react-redux";
import { setLoading } from "../../store/features/common";
import { fetchUserDetails } from "../../api/userInfo";
import { selectCartItems } from "../../store/features/cart";

const stripePublishableKey =
  process.env.STRIPE_KEY ||
  "pk_test_51Q8fqlFlYaAzWaPlE2uOTmOHghvSEyfK7dAmaswkEpjXDD6qmHvrKVUsI9m3gsMNAV51lQWi2rrzRXShejEbpTbM000bQbcH2t";
const stripePromise = loadStripe(stripePublishableKey);
const Payment = (props) => {
  const options = {
    mode: "payment",
    amount: 100,
    currency: "usd",
    appearance: {
      theme: "flat",
    },
  };
  return (
    <div>
      <Elements stripe={stripePromise} options={options}>
        <CheckOutPayment {...props} />
      </Elements>
    </div>
  );
};

export default Payment;
