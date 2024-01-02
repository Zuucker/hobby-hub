import { Notification, NotificationType } from "../scripts/Types";

function NotificationComponent(props: Notification) {
  let url = "";
  if (props.type === NotificationType.ammount_of_likes)
    url = "/post/" + props.postId;

  if (props.type === NotificationType.comment_reply)
    url = "/post/" + props.postId;

  if (props.type === NotificationType.group_join)
    url = "/group/" + props.groupName;

  if (props.type === NotificationType.user_mention)
    url = "/post/" + props.postId;

  return (
    <div className="col-12 d-flex justify-content-center">
      {props.type === NotificationType.ammount_of_likes && (
        <div className="notification col-12">
          <a href={url}>Your post got {props.likesAmmount} likes!</a>
        </div>
      )}

      {props.type === NotificationType.group_join && (
        <div className="notification col-12">
          <a href={url}>You joined {props.groupName} !</a>
        </div>
      )}

      {props.type === NotificationType.user_mention && (
        <div className="notification col-12">
          <a href={url}>{props.username} mentioned you!</a>
        </div>
      )}

      {props.type === NotificationType.comment_reply && (
        <div className="notification col-12">
          <a href={url}>{props.username} replied to your comment!</a>
        </div>
      )}

      {props.type === NotificationType.registered && (
        <div className="notification col-12">{props.content}</div>
      )}
    </div>
  );
}

export default NotificationComponent;
