import "../styles/Comment.css";
import { Comment } from "../scripts/Types";
import CommentToolBar from "./CommentToolBar";
import { v4 as uuidv4 } from "uuid";
import { useState } from "react";

function CommentComponent(props: Comment) {
  const [displayComment, setDisplayComment] = useState<boolean>(true);

  setTimeout(() => {
    const comments = document.getElementsByClassName("content");
    for (let i = 0; i < comments.length; i++) {
      const element = comments[i] as Element; // Casting to Element
      element.innerHTML = element.innerHTML.replaceAll(
        /@[a-zA-Z0-9_]+/g,
        (match) => `<span class='highlighted'>${match}</span>`
      );
    }
  }, 100);

  return (
    <div className="comment-wrapper d-flex">
      <div
        className="comment-hide-button pointer"
        onClick={() => {
          setDisplayComment(!displayComment);
        }}></div>
      {displayComment && (
        <div className="comment">
          <div className="username">{props.author}</div>
          <div className="content">{props.content}</div>
          <CommentToolBar
            points={props.points}
            interacted={props.interacted}
            id={props.id}
            upvoted={props.upvoted}
            authorId={props.authorId}
          />
          <div className="sub-comment" style={{ marginLeft: "20px" }}>
            {props.subcomments &&
              props.subcomments.length > 0 &&
              props.subcomments.map((c: Comment) => (
                <CommentComponent key={uuidv4()} {...c} />
              ))}
          </div>
        </div>
      )}
      {!displayComment && <div>Show comment</div>}
    </div>
  );
}

export default CommentComponent;
