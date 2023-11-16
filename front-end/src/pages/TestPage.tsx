import { useEffect } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { readCookie, setCookie } from "../scripts/Cookies";
import { Endpoints } from "../scripts/Types";

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

  axiosInstance
    .post(Endpoints.addGroup, {
      groupOwnerId: 1,
      groupName: "testTreeGroup",
      groupDescription: "trees are nicee",
    })
    .then((response) => {
      console.log("group adding", response.data);
    })
    .catch((e) => {
      console.log(e.message);
    });

  axiosInstance
    .post(Endpoints.subscribeToGroup, {
      id: 1,
      groupId: 4,
    })
    .then((response) => {
      console.log("group subs", response.data);
    })
    .catch((e) => {
      console.log(e.message);
    });

  axiosInstance
    .post(Endpoints.getUserGroups, {
      id: 1,
    })
    .then((response) => {
      console.log("user groups", response.data);
    })
    .catch((e) => {
      console.log(e.message);
    });

  return <div>test page</div>;
};

export default TestPage;
