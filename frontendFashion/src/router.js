import { createBrowserRouter } from "react-router-dom";
import Shop from "./Shop";
import ProductListPage from "./pages/ProductListPage/ProductListPage";
import ShopApplicationWrapper from "./pages/ShopApplicationWrapper";
import ProductDetails from "./pages/ProductDetailPage/ProductDetails";
import { loadProductBySlug } from "./routes/products";
import AuthenticationWrapper from "./pages/AuthenticationWrapper";
import Login from "./pages/Login/Login";
import Register from "./pages/Register/Register";
import Oauth2LoginCallback from "./pages/Oauth2LoginCallback";
import Cart from "./pages/Cart/Cart";
import AccCount from "./pages/AccCount/AccCount";
import ProtectedRoute from "./components/ProtectedRoute/ProtectedRoute";
import CheckOut from "./pages/CheckOut/CheckOut";
import Payment from "./pages/Payment/Payment";
import ConfirmPayment from "./pages/ConfirmPayment/ConfirmPayment";
import OrderConfirmed from "./pages/OrderConfirmed/OrderConfirmed";
import Profile from "./pages/AccCount/Profile";
import Orders from "./pages/AccCount/Orders";
import Setting from "./pages/AccCount/Setting";
import AdminPanel from "./pages/AdminPanel/AdminPanel";
import OrderCancel from "./pages/OrderConfirmed/OrderCancel";
import OrderSuccess from "./pages/OrderConfirmed/OrderSuccess";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <ShopApplicationWrapper />,
    children: [
      {
        path: "/",
        element: <Shop />,
      },
      {
        path: "/women",
        element: <ProductListPage categoryType={"WOMEN"} />,
      },
      {
        path: "/men",
        element: <ProductListPage categoryType={"MEN"} />,
      },
      {
        path: "/kids",
        element: <ProductListPage categoryType={"KIDS"} />,
      },
      {
        path: "/product/:slug",
        loader: loadProductBySlug,
        element: <ProductDetails />,
      },
      {
        path: "/cart-items",
        element: <Cart />,
      },
      {
        path: "/account-details/",
        element: (
          <ProtectedRoute>
            <AccCount />
          </ProtectedRoute>
        ),
        children: [
          {
            path: "profile",
            element: (
              <ProtectedRoute>
                <Profile />
              </ProtectedRoute>
            ),
          },
          {
            path: "orders",
            element: (
              <ProtectedRoute>
                <Orders />
              </ProtectedRoute>
            ),
          },
          {
            path: "settings",
            element: (
              <ProtectedRoute>
                <Setting />
              </ProtectedRoute>
            ),
          },
        ],
      },
      {
        path: "/checkout",
        element: (
          <ProtectedRoute>
            <CheckOut />
          </ProtectedRoute>
        ),
      },

      {
        path: "/orderConfirmed",
        element: <OrderConfirmed />,
      },
      {
        path: "/order-cancel",
        element: <OrderCancel />,
      },
      { path: "/success", element: <OrderSuccess /> },
    ],
  },
  {
    path: "/v1/",
    element: <AuthenticationWrapper />,
    children: [
      {
        path: "login",
        element: <Login />,
      },
      {
        path: "register",
        element: <Register />,
      },
    ],
  },
  {
    path: "/oauth2/callback",
    element: <Oauth2LoginCallback />,
  },
  {
    path: "/confirmPayment",
    element: <ConfirmPayment />,
  },
  {
    path: "/admin/*",
    element: (
      <ProtectedRoute>
        <AdminPanel />
      </ProtectedRoute>
    ),
  },
]);
