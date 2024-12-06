import React from "react";
import {
  Admin,
  AppBar,
  fetchUtils,
  Layout,
  Resource,
  useLogout,
  withLifecycleCallbacks,
} from "react-admin";
import simpleRestProvider from "ra-data-simple-rest";
import ProductList from "./ProductList";
import UpdateProduct from "./UpdateProduct";
import CreateProduct from "./CreateProduct";
import CategoryList from "./Category/CategoryList";
import UpdateCategory from "./Category/UpdateCategory";
import { fileUploadAPI } from "../../api/fileUpload";
import CreateCategory from "./Category/CreateCategory";
// const CDN_URL = "https://bluechicbucket.b-cdn.net";

const httpClient = (url, options = {}) => {
  const token = localStorage.getItem("authToken");
  if (!options.headers) options.headers = new Headers();
  options.headers.set("Authorization", `Bearer ${token}`);
  return fetchUtils.fetchJson(url, options);
};
const dataProvider = withLifecycleCallbacks(
  simpleRestProvider("http://localhost:8082/api", httpClient),
  [
    {
      resource: "products",
      beforeSave: async (params, dataProvider) => {
        console.log("Params ", params);
        let requestBody = {
          ...params,
        };
        let thumbnailResponse;
        if (params?.thumbnail?.rawFile) {
          const formData = new FormData();
          formData.append("file", params?.thumbnail?.rawFile);

          thumbnailResponse = await fileUploadAPI(formData);
          requestBody.thumbnail = thumbnailResponse; // Sử dụng URL từ Cloudinary
        }

        // Upload từng file trong `productResources` lên Cloudinary
        const productResList = params?.productResources ?? [];
        const newProductResList = await Promise.all(
          productResList.map(async (productResource) => {
            if (productResource?.url?.rawFile) {
              const formData = new FormData();
              formData.append("file", productResource?.url?.rawFile);

              const fileUploadRes = await fileUploadAPI(formData);
              return {
                ...productResource,
                url: fileUploadRes, // Sử dụng URL từ Cloudinary
              };
            }
            return productResource;
          })
        );

        // Gửi request cuối cùng
        const request = {
          ...requestBody,
          productResources: newProductResList,
        };

        console.log("Request Body ", request);
        return request;
      },
    },
  ]
);

// Nút Logout Tùy Chỉnh
const CustomLogoutButton = () => {
  const logout = useLogout();

  const handleLogout = () => {
    logout(); // Đăng xuất và chuyển hướng về trang đăng nhập
    localStorage.removeItem("authToken"); // Xóa token khỏi localStorage
  };

  return (
    <button
      onClick={handleLogout}
      style={{
        background: "white",
        color: "#2196f3",
        padding: "4px 8px", // Giảm padding để nút nhỏ hơn
        fontSize: "14px", // Giảm kích thước chữ
        border: "none", // Loại bỏ viền
        borderRadius: "4px",
        cursor: "pointer",
        marginLeft: "auto",
        boxShadow: "0px 2px 4px rgba(0, 0, 0, 0.1)", // Bóng mờ nhẹ ban đầu
        transition: "all 0.3s ease", // Hiệu ứng mượt mà
      }}
      onMouseOver={(e) => {
        e.target.style.transform = "scale(1.1)"; // Phóng to nhẹ
        e.target.style.boxShadow = "0px 4px 8px rgba(0, 0, 0, 0.3)"; // Tăng bóng mờ
      }}
      onMouseOut={(e) => {
        e.target.style.transform = "scale(1)"; // Quay lại kích thước ban đầu
        e.target.style.boxShadow = "0px 2px 4px rgba(0, 0, 0, 0.1)"; // Quay lại bóng mờ ban đầu
      }}
    >
      Log Out
    </button>
  );
};

// Tùy Chỉnh AppBar
const CustomAppBar = (props) => (
  <AppBar {...props}>
    <CustomLogoutButton />
  </AppBar>
);

// Layout Tùy Chỉnh
const CustomLayout = (props) => <Layout {...props} appBar={CustomAppBar} />;

const AdminPanel = () => {
  return (
    <Admin dataProvider={dataProvider} basename="/admin" layout={CustomLayout}>
      <Resource
        name="products"
        list={ProductList}
        edit={UpdateProduct}
        create={CreateProduct}
      />
      <Resource
        name="category"
        list={CategoryList}
        edit={UpdateCategory}
        create={CreateCategory}
      />
    </Admin>
  );
};

export default AdminPanel;
