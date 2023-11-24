import { useEffect, useState } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { readCookie } from "../scripts/Cookies";
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
      groupName: "betterTrees",
      groupDescription: "trees are niceeer",
    })
    .then((response) => {
      console.log("group adding", response.data);
    })
    .catch((e) => {
      console.log(e.message);
    });

  axiosInstance
    .post(Endpoints.subscribeToGroup, {
      id: 2,
      groupId: 3,
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

  return (
    <div>
      test page
      <img src="media/posts/image/xd1.jpg" alt="post"></img>
      <video controls>
        <source src="media/posts/video/xd1.mp4" type="video/mp4" />
        Your browser does not support the video tag.
      </video>
    </div>
  );
};

export default TestPage;
