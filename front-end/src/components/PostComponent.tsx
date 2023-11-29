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
        <div className="post-author">{props.author}</div>
      </div>
      <div className="col d-flex justify-content-between">
        <div className="post-title">{props.title}</div>
        <div className="">{props.group}</div>
      </div>
      {props.type === "image" && (
        <div className="content">
          <img src={props.link}></img>
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
