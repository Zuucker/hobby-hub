import { useEffect, useState } from "react";
import { Post } from "../scripts/Types";
import PostToolBar from "./PostToolBar";
import { TextField } from "@mui/material";

function PostComponent(props: Post) {
  const [postText, setPostText] = useState<string>("");

  useEffect(() => {
    if (props.type === "text") {
      fetch("../media/posts/text/xd1.txt")
        .then((response) => response.text())
        .then((data) => {
          setPostText(data);
        })
        .catch((error) => {
          console.error("Error fetching text file:", error);
        });
    }
  }, []);

  return (
    <div className="post">
      <div className="col d-flex justify-content-end">
        <div className="post-author">
          <a href={"http://localhost:3000/profile/" + props.author}>
            {props.author}
          </a>
        </div>
      </div>
      <div className="col d-flex justify-content-between">
        <div className="post-title">{props.title}</div>
        <div className="">
          <a href={"http://localhost:3000/group/" + props.group}>
            {props.group}
          </a>
        </div>
      </div>
      {props.type === "image" && (
        <div className="content">
          <img src={props.link} alt="post"></img>
        </div>
      )}

      {props.type === "text" && (
        <div className="content col">
          <TextField value={postText} disabled multiline />
        </div>
      )}

      {props.type === "video" && (
        <div className="content">
          <video controls>
            <source src={props.link} type="video/mp4" />
            Your browser does not support the video tag.
          </video>
        </div>
      )}

      <PostToolBar
        upVotes={props.upVotes}
        downVotes={props.downVotes}
        id={props.id}
        isUpVoted={props.isUpVoted}
        isDownVoted={props.isDownVoted}
      />
    </div>
  );
}

export default PostComponent;
