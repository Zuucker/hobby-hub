import { RouteObject } from "react-router";
import TestPage from "../pages/TestPage";
import RegisterPage from "../pages/RegisterPage";
import LoginPage from "../pages/LoginPage";
import VerificationPage from "../pages/VerificationPage";
import ProfilePage from "../pages/ProfilePage";

const paths: RouteObject[] = [
  {
    path: "/",
    element: <div>main page</div>,
  },
  {
    path: "/test",
    element: <TestPage />,
  },
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
    path: "/profile/:user",
    element: <ProfilePage />,
  },
];

export default paths;
