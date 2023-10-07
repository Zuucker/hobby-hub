import { RouteObject } from "react-router";
import TestPage from "../pages/TestPage";

const paths: RouteObject[] = [
  {
    path: "/",
    element: <div>main page</div>,
  },
  {
    path: "/test",
    element: <TestPage />,
  },
];

export default paths;
