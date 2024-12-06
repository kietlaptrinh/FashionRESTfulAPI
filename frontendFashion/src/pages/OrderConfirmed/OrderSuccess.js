import React, { useMemo } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const OrderSuccess = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const orderId = useMemo(() => {
    const query = new URLSearchParams(location.search);
    const orderId = query.get("orderId");
    return orderId;
  }, [location.search]);

  return (
    <div className="text-center p-8">
      <h1 className="text-2xl font-bold text-green-500">Payment Successful!</h1>
      <p>
        Your order has been placed successfully. Thank you for your submission
      </p>
      <button
        className="mt-4 bg-[#fff9c4] text-black py-2 px-4 rounded"
        onClick={() => navigate("/")}
      >
        Go to Home
      </button>
    </div>
  );
};

export default OrderSuccess;
