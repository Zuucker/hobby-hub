import { Notification, NotificationType } from "../scripts/Types";
function NotificationComponent(props: Notification) {
  return (
    <div className="col-12 d-flex justify-content-center">
      {props.type === NotificationType.ammount_of_likes && (
        <div className="notification col-12">
          <a href={props.url}>Your post got {props.likesAmmount} likes!</a>
        </div>
      )}

      {props.type === NotificationType.group_join && (
        <div className="notification col-12">
          <a href={props.url}>You joined {props.groupName} !</a>
        </div>
      )}

      {props.type === NotificationType.user_mention && (
        <div className="notification col-12">
          <a href={props.url}>{props.username} mentioned you!</a>
        </div>
      )}

      {props.type === NotificationType.comment_reply && (
        <div className="notification col-12">
          <a href={props.url}>{props.username} replied to your comment!</a>
        </div>
      )}
    </div>
  );
}

export default NotificationComponent;
