import { RouteObject } from "react-router";
import TestPage from "../pages/TestPage";
import RegisterPage from "../pages/RegisterPage";
import LoginPage from "../pages/LoginPage";

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
];

export default paths;
