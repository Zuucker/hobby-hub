import "../styles/Post.css";
import { useEffect, useState } from "react";
import EmptyPage from "../components/EmptyPageComponnent";
import { Endpoints, Post } from "../scripts/Types";
import axiosInstance from "../scripts/AxiosInstance";
import PostComponent from "../components/PostComponent";

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
            link: "../media/posts/image/" + responseData.id + ".jpg",
          };

          console.log(postData);

          setPost(postData);
        }
      })
      .then(() => {
        axiosInstance
          .post(Endpoints.getPostComments, { postId: postId })
          .then((response) => {
            const responseData = response.data.data.comments;

            console.log(responseData);

            setComments(responseData);
          });
      });
  }, []);

  return (
    <EmptyPage>
      <div className="post-container container-fluid">
        <PostComponent {...post} />
        <div className="comment-section">comment section XD</div>
      </div>
    </EmptyPage>
  );
}

export default PostPage;
