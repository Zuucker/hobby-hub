import { RouteObject } from "react-router";
import TestPage from "../pages/TestPage";
import RegisterPage from "../pages/RegisterPage";

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
];

export default paths;
