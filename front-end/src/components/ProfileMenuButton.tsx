import NotificationsIcon from "@mui/icons-material/Notifications";

interface ProfileMenuButtonProps {
  username: string;
  profilePicture: string;
  callback: () => void;
}

function ProfileMenuButton(props: ProfileMenuButtonProps) {
  const closeFunction = (event: MouseEvent) => {
    const profileButton = document.getElementById(
      "profileButton"
    ) as HTMLElement;
    const profileMenu = document.getElementById("profileMenu") as HTMLElement;

    const profileMenuRect = profileMenu?.getBoundingClientRect();
    const clickX = event.clientX;
    const clickY = event.clientY;

    if (profileMenu) {
      const proifleButtonRect = profileButton.getBoundingClientRect();
      if (
        (clickX > profileMenuRect.left &&
          clickX < profileMenuRect.right &&
          clickY > profileMenuRect.top &&
          clickY < profileMenuRect.bottom) ||
        (clickX > proifleButtonRect.left &&
          clickX < proifleButtonRect.right &&
          clickY > proifleButtonRect.top &&
          clickY < proifleButtonRect.bottom)
      ) {
      } else {
        props.callback();
        window.removeEventListener("click", closeFunction);
      }
    }
  };

  window.addEventListener("click", closeFunction);

  return (
    <div
      className="profile-menu-button col-1-5 d-flex justify-content-center align-bottom"
      id="profileButton"
      onClick={props.callback}>
      <NotificationsIcon className="notification-bell" />
      <span className="col d-flex justify-content-center">
        {props.username}
      </span>
      <img
        src={props.profilePicture}
        className="profile-picture"
        alt="avatar"></img>
    </div>
  );
}

export default ProfileMenuButton;
