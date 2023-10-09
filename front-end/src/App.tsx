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
    </>
  );
}

export default App;
