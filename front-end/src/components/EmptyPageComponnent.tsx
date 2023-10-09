import "../styles/ContentContainer.css";
import { ReactNode, useEffect } from "react";

interface EmptyPageProps {
  children?: ReactNode;
}

function EmptyPage(props: EmptyPageProps) {
  useEffect(() => {
    const contentContainer = document.getElementsByClassName(
      "content-container"
    )[0] as HTMLElement;

    const viewPortHeight = window.innerHeight;
    const navbar = document.getElementById("navbar") as HTMLElement;
    const navbaraHeight = navbar.getBoundingClientRect().height;

    contentContainer.style.minHeight = viewPortHeight - navbaraHeight + "px";
  }, []);

  return (
    <div className="d-flex justify-content-center">
      <div className="content-container d-flex justify-content-center align-items-center">
        {props.children}
      </div>
    </div>
  );
}

export default EmptyPage;
