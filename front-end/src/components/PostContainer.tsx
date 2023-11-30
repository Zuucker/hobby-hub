import "../styles/PostContainer.css";
import { Post } from "../scripts/Types";
import PostComponent from "./PostComponent";
import { v4 as uuidv4 } from "uuid";

type PostContainerProps = {
  posts: Post[];
};

function PostContainer(props: PostContainerProps) {
  return (
    <div className="col">
      <div className="post-container d-collumn align-items-center">
        {props.posts &&
          props.posts.map((a) => <PostComponent key={uuidv4()} {...a} />)}
        {(!props.posts || !props.posts.length || props.posts.length === 0) && (
          <div className="d-flex justify-content-center align-items-center">
            <div>There are no post here yet!</div>
          </div>
        )}
      </div>
    </div>
  );
}

export default PostContainer;
