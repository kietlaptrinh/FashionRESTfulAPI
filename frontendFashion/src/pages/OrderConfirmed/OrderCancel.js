import React from "react";
import { useNavigate } from "react-router-dom";

const OrderCancel = () => {
  const navigate = useNavigate();

  return (
    <div className="text-center p-8">
      <h1 className="text-2xl font-bold text-red-500">Payment Cancelled!</h1>
      <p>Your payment process was cancelled.</p>
      <button
        className="mt-4 bg-blue-500 text-white py-2 px-4 rounded"
        onClick={() => navigate("/checkout")}
      >
        Try Again
      </button>
    </div>
  );
};

export default OrderCancel;
