import "../styles/PostContainer.css";
import { Post } from "../scripts/Types";
import PostComponent from "./PostComponent";

type PostContainerProps = {
  posts: Post[];
};

function PostContainer(props: PostContainerProps) {
  return (
    <div className="col">
      <div className="post-container d-collumn align-items-center">
        {props.posts.map((a) => (
          <PostComponent {...a} />
        ))}
      </div>
    </div>
  );
}

export default PostContainer;
