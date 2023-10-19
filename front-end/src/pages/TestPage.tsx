import { useEffect } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { readCookie, setCookie } from "../scripts/Cookies";

const TestPage = () => {
  useEffect(() => {
    axiosInstance
      .get("/test")
      .then((response) => {
        //if (response.status === 200) setMessage(response.data.message);
        console.log(response.data.message);
      })
      .catch((e) => {
        console.log(e.message);
      });

    axiosInstance
      .post("/auth/usernameAvailability", {
        username: "user1",
      })
      .then((response) => {
        console.log("username available", response.data);
      })
      .catch((e) => {
        console.log(e.message);
      });

    axiosInstance
      .post("/auth/register", {
        username: "user15",
        password: "password15",
        passwordConfirmation: "password15",
        email: "email15",
      })
      .then((response) => {
        console.log("register user", response.data);
      })
      .catch((e) => {
        console.log(e.message);
      });

    axiosInstance
      .post("/auth/login", {
        username: "user1",
        password: "password1",
      })
      .then((response) => {
        console.log("login user", response.data);
      })
      .catch((e) => {
        console.log(e.message);
      });

    axiosInstance
      .post("/auth/isVerified", {
        username: "user2",
      })
      .then((response) => {
        console.log("is user2 verified?", response.data);
      })
      .catch((e) => {
        console.log(e.message);
      });

    axiosInstance
      .post("/auth/verify", {
        code: window.location.href.split("/").pop(),
      })
      .then((response) => {
        console.log("verify", response.data);
      })
      .catch((e) => {
        console.log(e.message);
      });

    readCookie("test");
  }, []);

  return <div>test page</div>;
};

export default TestPage;
