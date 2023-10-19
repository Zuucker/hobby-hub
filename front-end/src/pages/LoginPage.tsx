import "../styles/Login.css";
import EmptyPage from "../components/EmptyPageComponnent";
import { TextField } from "@mui/material";
import { useState } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints } from "../scripts/Types";
import { setCookie } from "../scripts/Cookies";

type LoginData = {
  username: string;
  password: string;
};

function LoginPage() {
  const [message, setMessage] = useState<string>("");
  const [inputData, setInputData] = useState<LoginData>({
    username: "",
    password: "",
  });

  const sendLoginRequest = () => {
    if (inputData.password !== "" && inputData.username !== "")
      axiosInstance.post(Endpoints.loginUser, inputData).then((response) => {
        if (response.data.status) {
          setMessage("Logged in successfully");
          setCookie("jwtToken", response.data.value);
          setTimeout(() => {
            window.location.href = "/test";
          }, 100);
        } else setMessage(response.data.value);
      });
    else {
      setMessage("Please provide valid credentials");
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    switch (e.target.placeholder) {
      case "Username":
        setInputData({ ...inputData, username: e.target.value });
        break;

      case "Password":
        setInputData({ ...inputData, password: e.target.value });
        break;

      default:
        break;
    }
  };

  return (
    <EmptyPage>
      <div className="login-container d-flex justify-content-center">
        <div className="col-8">
          <span className="d-flex justify-content-center">Login</span>
          <TextField
            placeholder="Username"
            onChange={handleChange}
            className="col-12"
          />

          <TextField
            placeholder="Password"
            onChange={handleChange}
            type="password"
            className="col-12"
          />

          <span
            className={
              message !== ""
                ? "error-message d-flex justify-content-center"
                : "invisible"
            }>
            {message}
          </span>

          <div className="button-container col-12 d-flex justify-content-center">
            <div
              className="col-12 btn-purple d-flex justify-content-center align-items-center"
              onClick={() => {
                sendLoginRequest();
              }}>
              Login
            </div>
          </div>
        </div>
      </div>
    </EmptyPage>
  );
}

export default LoginPage;
