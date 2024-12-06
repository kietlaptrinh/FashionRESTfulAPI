import React, { useEffect, useMemo, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { selectCartItems } from "../../store/features/cart";
import { useNavigate } from "react-router-dom";
import { setLoading } from "../../store/features/common";
import { fetchUserDetails } from "../../api/userInfo";
import Payment from "../Payment/Payment";
import PayPalButton1 from "../../components/PayPalButton";
import { createOrderRequest } from "../../utils/OrderUtil";
import axios from "axios";
// import { PayPalButtons, PayPalScriptProvider } from "@paypal/react-paypal-js";

const CheckOut = () => {
  const cartItems = useSelector(selectCartItems);
  const dispatch = useDispatch();
  const [userInfo, setUserInfo] = useState([]);
  const navigate = useNavigate();
  const [paymentMethod, setPaymentMethod] = useState("");

  const subTotal = useMemo(() => {
    let value = 0;
    cartItems?.forEach((element) => {
      value += element?.subTotal;
    });
    return value?.toFixed(2);
  }, [cartItems]);

  useEffect(() => {
    dispatch(setLoading(true));
    fetchUserDetails()
      .then((res) => {
        setUserInfo(res);
      })
      .catch((err) => {})
      .finally(() => {
        dispatch(setLoading(false));
      });
  }, [dispatch]);

  //paypal
  const handleSuccess = (order) => {
    console.log("PayPal Order Successful:", order);
    alert(`Payment Successful! Transaction ID: ${order.id}`);

    // Điều hướng hoặc xử lý sau thanh toán
    navigate("/success");
  };

  //logic handle palpal
  const handlePayPalPayment = async () => {
    try {
      dispatch(setLoading(true));

      // Gọi API backend để tạo đơn hàng
      const response = await axios.post("/api/orders", {
        paymentMethod: "PAYPAL",
      });

      if (response?.credentials?.approvalLink) {
        // Chuyển hướng đến PayPal để thanh toán
        window.location.href = response.credentials.approvalLink;
      } else {
        alert("Failed to get approval link from PayPal!");
      }
    } catch (error) {
      console.error("Error during PayPal payment initialization: ", error);
      alert("Something went wrong!");
    } finally {
      dispatch(setLoading(false));
    }
  };

  // Xử lý kết quả thanh toán sau khi PayPal chuyển hướng
  useEffect(() => {
    const queryParams = new URLSearchParams(window.location.search);
    const paymentStatus = queryParams.get("paymentStatus");

    if (paymentStatus === "success") {
      navigate("/success");
    } else if (paymentStatus === "cancel") {
      navigate("/order-cancel");
    }
  }, [navigate]);

  // const [payPalTransactionSuccess, setPayPalTransactionSuccess] =
  //   useState(false);
  // const [transactionId, setTransactionId] = useState("");

  return (
    <div className="p-8 flex">
      <div className="w-[70%]">
        <div className="flex gap-8">
          {/* Address */}
          <p className="font-bold">Delivery address</p>
          {userInfo?.addressList && (
            <div>
              <p>{userInfo?.addressList?.[0]?.name}</p>
              <p>{userInfo?.addressList?.[0]?.street}</p>
              <p>
                {userInfo?.addressList?.[0]?.city},
                {userInfo?.addressList?.[0]?.state}{" "}
                {userInfo?.addressList?.[0]?.zipCode}
              </p>
            </div>
          )}
        </div>
        <hr className="h-[2px] bg-slate-200 w-[90%] my-4"></hr>
        <div className="flex gap-8 flex-col">
          {/* Address */}
          <p className="font-bold">Choose delivery</p>
        </div>
        <div>
          <p>Select a day</p>
          <div className="flex gap-4 mt-4">
            <div
              className="w-[80px] h-[48px] flex flex-col justify-center border text-center mb-4 rounded-lg mr-4 cursor-pointer
                   hover:scale-110 bg-[#fff9c4] border-gray-500 text-gray-500"
            >
              <p className="text-center">{"Nov 15"}</p>
            </div>
            <div
              className="w-[80px] h-[48px] flex flex-col justify-center border text-center mb-4 rounded-lg mr-4 cursor-pointer
                   hover:scale-110 bg-white border-gray-500 text-gray-500"
            >
              <p className="text-center">{"Nov 18"}</p>
            </div>
          </div>
        </div>
        <hr className="h-[2px] bg-slate-200 w-[90%] my-4"></hr>
        <div className="flex flex-col gap-2">
          <p className="font-bold">Payment Method</p>
          <div className="mt-4 flex flex-col gap-4">
            <div className="flex gap-2">
              <input
                type="radio"
                name="payment_mathod"
                value={"CARD"}
                onChange={() => setPaymentMethod("CARD")}
              />
              <p> Credit/Debit Card</p>
            </div>
            <div className="flex gap-2">
              <input
                type="radio"
                name="payment_mathod"
                value={"PAYPAL"}
                onChange={() => setPaymentMethod("PAYPAL")}
              />
              <p> PayPal</p>
            </div>

            <div className="flex gap-2">
              <input
                type="radio"
                name="payment_mathod"
                value={"COD"}
                onChange={() => setPaymentMethod("COD")}
              />
              <p> Cash on delivery</p>
            </div>
            <div className="flex gap-2">
              <input
                type="radio"
                name="payment_mathod"
                value={"UPI"}
                onChange={() => setPaymentMethod("COD")}
              />
              <p> UPI/Wallet</p>
            </div>
          </div>
        </div>
        {paymentMethod === "PAYPAL" && (
          <PayPalButton1
            onClick={handlePayPalPayment}
            totalAmount={subTotal}
            onSuccess={handleSuccess}
          />
        )}
        {/* {paymentMethod === "PAYPAL" && (
  <div className="mt-4">
    <PayPalScriptProvider
      options={{
        "client-id": "YOUR_PAYPAL_CLIENT_ID", // Thay bằng Client ID từ PayPal Developer
        currency: "USD",
      }}
    >
      <PayPalButtons
        style={{ layout: "vertical" }}
        createOrder={(data, actions) => {
          return actions.order.create({
            purchase_units: [
              {
                amount: {
                  value: subTotal, // Tổng giá trị giỏ hàng
                },
              },
            ],
          });
        }}
        onApprove={async (data, actions) => {
          return actions.order.capture().then((details) => {
            const { id } = details.purchase_units[0].payments.captures[0];
            setPayPalTransactionSuccess(true);
            setTransactionId(id);
            alert(`Transaction completed! Transaction ID: ${id}`);
          });
        }}
        onError={(err) => {
          console.error("PayPal Checkout Error: ", err);
          alert("Something went wrong with PayPal Checkout!");
        }}
      />
    </PayPalScriptProvider>
  </div>
)} */}

        {paymentMethod === "CARD" && (
          <Payment
            userId={userInfo?.id}
            addressId={userInfo?.addressList?.[0]?.id}
          />
        )}

        {paymentMethod !== "CARD" && (
          <button
            className="w-[150px] items-center h-[48px] bg-[#fff9c4] border rounded-lg mt-4 text-black hover:bg-black hover:text-[#fff9c4]"
            onClick={() => navigate("/payment")}
          >
            Pay Now
          </button>
        )}
      </div>
      <div className=" w-[30%] h-[30%] border rounded-lg border-gray-500 hover:bg-[#fff9c4] p-4 flex flex-col gap-4">
        <p>Order Summary</p>
        <p>Items Count = {cartItems?.length}</p>
        <p>SubTotal = ${subTotal}</p>
        <p>Shipping = FREE</p>
        <hr className="h-[2px] bg-gray-400"></hr>
        <p>Total Amount = ${subTotal}</p>
      </div>
    </div>
  );
};

export default CheckOut;
