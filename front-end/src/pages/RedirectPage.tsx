import { useEffect } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { readCookie, setCookie } from "../scripts/Cookies";
import { BooleanLiteral } from "typescript";

type RedirectData = {
  isLoggedIn: boolean;
};

const RedirectPage = (props: RedirectData) => {
  useEffect(() => {
    if (props.isLoggedIn) window.location.href = "/";
    else window.location.href = "/login";
  }, []);

  return <div></div>;
};

export default RedirectPage;
