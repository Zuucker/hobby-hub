import { useEffect, useState } from "react";
import { Post } from "../scripts/Types";
import PostToolBar from "./PostToolBar";
import { TextField } from "@mui/material";

function PostComponent(props: Post) {
  const [postText, setPostText] = useState<string>("");

  useEffect(() => {
    if (props.type === "text") {
      fetch("../media/posts/text/" + props.id + ".txt")
        .then((response) => response.text())
        .then((data) => {
          setPostText(data);
        })
        .catch((error) => {
          console.error("Error fetching text file:", error);
        });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [props]);

  const shouldDisplaylink = !window.location.href.includes("post");

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

      {props.type === "image" && shouldDisplaylink && (
        <a href={"http://localhost:3000/post/" + props.id}>
          <div className="content">
            <img src={props.link} alt="post"></img>
          </div>
        </a>
      )}
      {props.type === "image" && !shouldDisplaylink && (
        <div className="content">
          <img src={props.link} alt="post"></img>
        </div>
      )}

      {props.type === "text" && shouldDisplaylink && (
        <a href={"http://localhost:3000/post/" + props.id}>
          <div className="content col">
            <TextField value={postText} size="small" disabled multiline />
          </div>
        </a>
      )}

      {props.type === "text" && !shouldDisplaylink && (
        <div className="content col">
          <TextField value={postText} disabled multiline />
        </div>
      )}

      {props.type === "video" && shouldDisplaylink && (
        <a href={"http://localhost:3000/post/" + props.id}>
          <div className="content">
            <video controls>
              <source src={props.link} type="video/mp4" />
              Your browser does not support the video tag.
            </video>
          </div>
        </a>
      )}

      {props.type === "video" && !shouldDisplaylink && (
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
