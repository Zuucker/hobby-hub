import { RouteObject } from "react-router";
import TestPage from "../pages/TestPage";
import RegisterPage from "../pages/RegisterPage";
import LoginPage from "../pages/LoginPage";
import VerificationPage from "../pages/VerificationPage";
import ProfilePage from "../pages/ProfilePage";
import { readCookie } from "./Cookies";
import RedirectPage from "../pages/RedirectPage";
import Logout from "../pages/Logout";
import GroupCreationPage from "../pages/GroupCreationPage";
import GroupPage from "../pages/GroupPage";
import PostPage from "../pages/PostPage";
import PostCreationPage from "../pages/PostCreationPage";

let paths: RouteObject[] = [];

const isLoggedIn = readCookie("jwtToken") ? true : false;

if (isLoggedIn) {
  paths = [
    {
      path: "/",
      element: <div>main page</div>,
    },
    {
      path: "/logout",
      element: <Logout />,
    },
    {
      path: "/test",
      element: <TestPage />,
    },
    {
      path: "/profile/:user",
      element: <ProfilePage />,
    },
    {
      path: "/createGroup",
      element: <GroupCreationPage />,
    },
    {
      path: "/group/:group",
      element: <GroupPage />,
    },
    {
      path: "/post/:post",
      element: <PostPage />,
    },
    {
      path: "/add",
      element: <PostCreationPage />,
    },
    {
      path: "*",
      element: <RedirectPage isLoggedIn={isLoggedIn} />,
    },
  ];
} else {
  paths = [
    {
      path: "/register",
      element: <RegisterPage />,
    },
    {
      path: "/login",
      element: <LoginPage />,
    },
    {
      path: "/verify/:code",
      element: <VerificationPage />,
    },
    {
      path: "*",
      element: <RedirectPage isLoggedIn={isLoggedIn} />,
    },
  ];
}

export default paths;
