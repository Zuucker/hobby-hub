import "./App.css";
import "./styles/Colors.css";
import "./styles/Global.css";
import Navbar from "./components/Navbar";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import paths from "./scripts/RouterPaths";

const router = createBrowserRouter(paths);

function App() {
  return (
    <>
      <Navbar />
      <RouterProvider router={router} />
      <div className="container main d-flex justify-content-center align-items-center">
        <h1 className="col-4 text-center d-flex justify-content-center align-items-center">
          henlo
        </h1>
      </div>
    </>
  );
}

export default App;
