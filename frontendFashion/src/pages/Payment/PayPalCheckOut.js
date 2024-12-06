// import { PayPalButtons } from "@paypal/react-paypal-js";
// import { confirmPaymentPayPalAPI } from "./api";

// const PayPalCheckOut = () => {
//   const handleSuccess = async (order) => {
//     const payload = {
//       paymentId: order.id,
//       payerId: order.payer.payer_id,
//       orderId: order.purchase_units[0].custom_id,
//     };

//     try {
//       const response = await confirmPaymentPayPalAPI(payload);
//       console.log("Payment confirmed: ", response);
//       // Cập nhật UI hoặc chuyển hướng
//     } catch (error) {
//       console.error("Payment confirmation failed: ", error.message);
//     }
//   };

//   return (
//     <PayPalButtons
//       createOrder={(data, actions) => {
//         return actions.order.create({
//           purchase_units: [
//             {
//               amount: {
//                 value: "100.00", // Giá trị thanh toán
//               },
//               custom_id: "ORDER1234", // ID đơn hàng của bạn
//             },
//           ],
//         });
//       }}
//       onApprove={(data, actions) => {
//         return actions.order.capture().then((order) => {
//           handleSuccess(order); // Gọi hàm xử lý khi thanh toán thành công
//         });
//       }}
//       onCancel={() => {
//         console.log("User cancelled the payment");
//       }}
//       onError={(err) => {
//         console.error("Payment error: ", err);
//       }}
//     />
//   );
// };

// export default PayPalCheckOut;
