import "../styles/Post.css";
import { useEffect, useState } from "react";
import EmptyPage from "../components/EmptyPageComponnent";
import { Comment, Endpoints, Post } from "../scripts/Types";
import axiosInstance from "../scripts/AxiosInstance";
import PostComponent from "../components/PostComponent";
import CommentComponent from "../components/CommentComponent";
import { v4 as uuidv4 } from "uuid";

function PostPage() {
  const [post, setPost] = useState<Post>({
    id: -1,
    title: "",
    authorId: -1,
    groupId: -1,
    group: "",
    type: "",
    link: "",
    upVotes: -1,
    downVotes: -1,
    isUpVoted: false,
    isDownVoted: false,
  });

  const [comments, setComments] = useState<Comment[]>([]);

  useEffect(() => {
    const postId = window.location.href.split("/").pop();

    axiosInstance
      .post(Endpoints.getPostData, { postId: postId })
      .then((response) => {
        const responseData = response.data.data.post;

        if (responseData) {
          const postData = {
            ...responseData,
            link:
              responseData.type === "image"
                ? "../media/posts/image/" + responseData.id + ".jpg"
                : "../media/posts/video/" + responseData.id + ".mp4",
          };

          setPost(postData);
        }
      })
      .then(() => {
        axiosInstance
          .post(Endpoints.getPostComments, { postId: postId })
          .then((response) => {
            const responseData = response.data.data.comments;

            setComments(responseData);
          });
      });
  }, []);

  return (
    <EmptyPage>
      <div className="post-container container-fluid">
        <PostComponent {...post} />
        <div className="comment-section d-collumn justify-content-center align-items-center">
          {comments &&
            comments.length > 0 &&
            comments.map((c: Comment) => (
              <CommentComponent key={uuidv4()} {...c} />
            ))}

          {(!comments || comments.length === 0) && (
            <div>There are no comments yet</div>
          )}
        </div>
      </div>
    </EmptyPage>
  );
}

export default PostPage;
