import "../styles/Register.css";
import EmptyPage from "../components/EmptyPageComponnent";
import { TextField } from "@mui/material";
import InputAdornment from "@mui/material/InputAdornment";
import CheckMark from "../icons/checkMark.svg";
import { useEffect, useState } from "react";
import * as yup from "yup";
import YupPassword from "yup-password";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints } from "../scripts/Types";

YupPassword(yup);

const checkmarkIcon = () => {
  return {
    endAdornment: (
      <InputAdornment position="start">
        <img
          id="usernameCheckmark"
          className="checkmark"
          src={CheckMark}
          alt="checkmark"></img>
      </InputAdornment>
    ),
  };
};

const errorMessageTranslator = (message: string) => {
  if (message === "") return message;

  const keyWords = [
    { old: /\bpassword\b/i, new: "Password" },
    { old: /\bconfirmPassword\b/, new: "Confirm Password" },
    {
      old: /be one of the following values: Ref\(Password\)/,
      new: "match the Password",
    },
    { old: /\email\b/, new: "Email" },
    { old: /\username\b/, new: "Username" },
  ];

  for (let i = 0; i < keyWords.length; i++) {
    message = message.replace(keyWords[i].old, keyWords[i].new);
  }

  return message;
};

let userSchema = yup.object({
  username: yup.string().required().min(4).max(12),
  email: yup.string().required().email(),
  password: yup
    .string()
    .required()
    .password()
    .minLowercase(1)
    .minUppercase(1)
    .minSymbols(1)
    .min(6)
    .max(20),
  confirmPassword: yup
    .string()
    .required()
    .oneOf([yup.ref("password")]),
});

type RegisterData = {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
};

function RegisterPage() {
  const [message, setMessage] = useState<string>("");
  const [isUsernameFree, setIsUsernameFree] = useState<boolean>(false);
  const [inputData, setInputData] = useState<RegisterData>({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  useEffect(() => {
    if (inputData.username !== "") {
      axiosInstance
        .post(Endpoints.checkUsernameAvailability, inputData)
        .then((response) => {
          setIsUsernameFree(response.data);
        });
    } else {
      const usernameCheckmark = document.getElementById(
        "usernameCheckmark"
      ) as HTMLElement;
      usernameCheckmark.style.visibility = "hidden";
      setIsUsernameFree(false);
    }
  }, [inputData, inputData.username]);

  useEffect(() => {
    const usernameCheckmark = document.getElementById(
      "usernameCheckmark"
    ) as HTMLElement;
    usernameCheckmark.style.visibility =
      isUsernameFree && inputData.username.length > 3 ? "visible" : "hidden";
  }, [isUsernameFree, inputData.username.length]);

  const validate = () => {
    userSchema
      .validate(inputData)
      .then(() => {
        axiosInstance
          .post(Endpoints.registerUser, inputData)
          .then((response) => {
            setMessage(response.data ? "Registered!" : "Try again later");
            if (response.data) window.location.href = "/login";
          });
      })
      .catch((e) => {
        const message = errorMessageTranslator(e.message);
        setMessage(message);
      });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    switch (e.target.placeholder) {
      case "Username":
        setInputData({ ...inputData, username: e.target.value });
        break;

      case "Email":
        setInputData({ ...inputData, email: e.target.value });
        break;

      case "Password":
        setInputData({ ...inputData, password: e.target.value });
        break;

      case "Confirm password":
        setInputData({ ...inputData, confirmPassword: e.target.value });
        break;

      default:
        break;
    }
  };

  return (
    <EmptyPage>
      <div className="register-container d-flex justify-content-center">
        <div className="col-8">
          <span className="d-flex justify-content-center">Register</span>
          <TextField
            placeholder="Username"
            InputProps={checkmarkIcon()}
            onChange={handleChange}
            className="col-12"
          />
          <TextField
            placeholder="Email"
            onChange={handleChange}
            className="col-12"
          />
          <TextField
            placeholder="Password"
            onChange={handleChange}
            type="password"
            className="col-12"
          />
          <TextField
            placeholder="Confirm password"
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
                validate();
              }}>
              Register
            </div>
          </div>
        </div>
      </div>
    </EmptyPage>
  );
}

export default RegisterPage;
