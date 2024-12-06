import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { saveToken } from "../utils/JwtHelper";

const Oauth2LoginCallback = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");

    if (token) {
      saveToken(token);
      navigate("/");
    } else {
      navigate("/v1/login");
    }
  }, [navigate]);
  return <div></div>;
};

export default Oauth2LoginCallback;
