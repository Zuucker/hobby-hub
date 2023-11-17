import { useEffect } from "react";
import { deleteCookie } from "../scripts/Cookies";

function LoginPage() {
  useEffect(() => {
    console.log("halo");
    deleteCookie("jwtToken");
    window.location.href = "/";
  }, []);

  return <div>Trying to log out</div>;
}

export default LoginPage;
