import { useEffect, useState } from "react";
import "./App.css";
import axiosInstance from "./scripts/AxiosInstance";

function App() {
  const [message, setMessage] = useState("");

  useEffect(() => {
    axiosInstance.get("/test").then((response) => {
      setMessage(response.data.message);
    });

    axiosInstance
      .post("/auth/usernameAvailability", {
        username: "user1",
      })
      .then((response) => {
        console.log("username available", response.data);
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
      });

    axiosInstance
      .post("/auth/login", {
        username: "user1",
        password: "password1",
      })
      .then((response) => {
        console.log("login user", response.data);
      });

    axiosInstance
      .post("/auth/isVerified", {
        username: "user2",
      })
      .then((response) => {
        console.log("is user2 verified?", response.data);
      });
  }, []);

  return (
    <div className="container main d-flex justify-content-center align-items-center">
      <h1 className="col-4 text-center d-flex justify-content-center align-items-center">
        {message}
      </h1>
    </div>
  );
}

export default App;
