import "../styles/ProfileMenu.css";
import { Notification } from "../scripts/Types";
import { v4 as uuidv4 } from "uuid";
import NotificationComponent from "./NotificationComponent";

interface ProfileMenuProps {
  isLoggedIn: boolean;
  notifications: Notification[];
  username: String;
}

function ProfileMenu(props: ProfileMenuProps) {
  const notifications = props.notifications;

  return (
    <>
      {props.isLoggedIn && (
        <div className="profile-menu-container justify-content-center">
          <div className="notification-container d-flex justify-content-center">
            <div className="notification-container-child d-collumn">
              <div className="d-flex justify-content-center">Notifications</div>
              {notifications.map((n) => (
                <NotificationComponent {...n} key={uuidv4()} />
              ))}
            </div>
          </div>
          <div className="profile-buttons col">
            <button
              className="btn-purple col-12"
              onClick={() => {
                window.location.href = "/profile/" + props.username;
              }}>
              Profile
            </button>
            <button
              className="btn-purple col-12"
              onClick={() => {
                window.location.href = "/group?create";
              }}>
              Create a new Group
            </button>
            <button
              className="btn-purple col-12"
              onClick={() => {
                window.location.href = "/logout";
              }}>
              Log out
            </button>
          </div>
        </div>
      )}
      {!props.isLoggedIn && (
        <>
          <div className="foreign-profile-menu bg-warning">
            <div className="profile-buttons col">
              <button
                className="btn-purple col-12"
                onClick={() => {
                  window.location.href = "/register";
                }}>
                Register
              </button>
              <button
                className="btn-purple col-12"
                onClick={() => {
                  window.location.href = "/login";
                }}>
                Login
              </button>
            </div>
          </div>
        </>
      )}
    </>
  );
}

export default ProfileMenu;
