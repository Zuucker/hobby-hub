import { useEffect, useState } from "react";
import "./App.css";
import axiosInstance from "./scripts/AxiosInstance";

function App() {
  const [message, setMessage] = useState("");

  useEffect(() => {
    axiosInstance.get("/test").then((response) => {
      setMessage(response.data.message);
    });
  }, []);

  return (
    <div className="container main d-flex justify-content-center align-items-center">
      <h1 className="col-4 text-center d-flex justify-content-center align-items-center">
        {message}
      </h1>
    </div>
  );
}

export default App;
