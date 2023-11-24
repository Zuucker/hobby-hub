import "../styles/Navbar.css";
import "../styles/MenuContainer.css";
import logo from "../icons/Logo.svg";
import { useState, useEffect } from "react";
import HamMenuIcon from "@mui/icons-material/Menu";
import ProfileMenuButton from "./ProfileMenuButton";
import SearchBar from "./SearchBar";
import SectionMenu from "./SectionMenu";
import ProfileMenu from "./ProfileMenu";
import { Endpoints, Notification, NotificationType } from "../scripts/Types";
import { readCookie } from "../scripts/Cookies";
import axiosInstance from "../scripts/AxiosInstance";

function Navbar() {
  const [isSectionMenuVisible, setIsSectionMenuVisible] = useState(false);
  const [isProfileMenuVisible, setIsProfileMenuVisible] = useState(false);
  const [notifications, setNotifications] = useState<Notification[]>([]);

  const [username, setUsername] = useState<string>("Sad Cat");
  const [profilePicture, setProfilePicture] =
    useState<string>("profile_pic.jpg");

  const [isLoggedIn, setIsLoggedIm] = useState<boolean>(
    readCookie("jwtToken") ? true : false
  );

  const toggleProfileMenuVisibility = () => {
    setIsProfileMenuVisible(!isProfileMenuVisible);
  };

  useEffect(() => {
    if (isSectionMenuVisible) {
      const navbar = document.getElementById("navbar") as HTMLElement;
      const sectionMenu = document.getElementsByClassName(
        "section-menu"
      )[0] as HTMLElement;
      const navbarHeight = navbar.getBoundingClientRect().height;
      const viewPortHeight = window.innerHeight;
      sectionMenu.style.height = viewPortHeight - navbarHeight + "px";
    }
  });

  useEffect(() => {
    const notificationBell = document.getElementsByClassName(
      "notification-bell"
    )[0] as HTMLElement;
    if (notificationBell)
      notificationBell.style.visibility =
        notifications.length > 0 ? "visible" : "hidden";
  }, [notifications]);

  useEffect(() => {
    if (isLoggedIn) {
      axiosInstance.post(Endpoints.getUserDataFromJwt).then((response) => {
        const data = response.data.data;
        if (data.username) setUsername(data.username);
        if (data.profilePic) setProfilePicture("../" + data.profilePic);
      });

      //NOTIFICATIONS
      // axiosInstance.post(Endpoints.getNotifications).then((response) => {
      //   const data = response.data.data;
      // });
    }

    const notification: Notification = {
      type: NotificationType.comment_reply,
      url: "/post/1",
      groupName: "groupname",
      username: "",
      likesAmmount: 20,
    };

    setNotifications([
      notification,
      notification,
      notification,
      notification,
      notification,
      notification,
      notification,
      notification,
      notification,
      notification,
      notification,
      notification,
      notification,
      notification,
      notification,
    ]);
  }, []);

  return (
    <>
      <div id="navbar" className="container-fluid d-flex align-items-center">
        <div className="section-menu-button col-0-5">
          <HamMenuIcon
            className="ham-menu"
            onClick={() => setIsSectionMenuVisible(!isSectionMenuVisible)}
          />
        </div>

        <div className="col-2 d-flex justify-content-center">
          <a href="/">
            <img src={logo} className="logo" alt="logo" />
          </a>
        </div>

        <div className="search-bar col d-flex justify-content-center">
          <SearchBar />
        </div>

        <ProfileMenuButton
          username={username}
          profilePicture={profilePicture}
          callback={toggleProfileMenuVisibility}
        />
      </div>

      <div id="menu-container" className=" container-fluid d-flex">
        {isSectionMenuVisible && (
          <div className="section-menu col-2 d-flex">
            <SectionMenu isLoggedIn={isLoggedIn} />
          </div>
        )}
        {isProfileMenuVisible && (
          <div
            className="profile-menu col-2 d-flex justify-content-center"
            id="profileMenu">
            <ProfileMenu
              isLoggedIn={isLoggedIn}
              notifications={notifications}
              username={username}
            />
          </div>
        )}
      </div>
    </>
  );
}

export default Navbar;
