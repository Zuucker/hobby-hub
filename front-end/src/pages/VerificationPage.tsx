import "../styles/Register.css";
import EmptyPage from "../components/EmptyPageComponnent";
import { useEffect, useState } from "react";
import { Endpoints } from "../scripts/Types";
import axiosInstance from "../scripts/AxiosInstance";

function VerificationPage() {
  const [message, setMessage] = useState<String>("Please wait a few seconds");

  useEffect(() => {
    const verificationData = { code: window.location.href.split("/").pop() };

    setTimeout(() => {
      axiosInstance
        .post(Endpoints.verifyUser, verificationData)
        .then((response) => {
          console.log(response.data);
          if (response.data.status) window.location.href = "/login";
          else {
            setMessage(
              "User is already verified or there is a problem with the service."
            );
          }
        });
    }, 100);
  }, []);

  return (
    <EmptyPage>
      <div className="register-container d-flex justify-content-center">
        <div className="col-8 d-flex justify-content-center align-items-center">
          {message}
        </div>
      </div>
    </EmptyPage>
  );
}

export default VerificationPage;
