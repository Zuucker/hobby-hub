import "../styles/MainPage.css";
import EmptyPage from "../components/EmptyPageComponnent";
import { useEffect, useState } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints, Post } from "../scripts/Types";
import PostContainer from "../components/PostContainer";

function MainPage() {
  const [posts, setPosts] = useState<Post[]>([]);

  useEffect(() => {
    axiosInstance.post(Endpoints.getUserFeed).then((response) => {
      const postsData: Post[] = [];
      const responseData = response.data.data.posts;
      if (responseData && responseData.length > 0) {
        responseData.forEach((p: Post) => {
          postsData.push({
            id: p.id,
            authorId: p.authorId,
            author: p.author,
            groupId: p.groupId,
            group: p.group,
            title: p.title,
            link:
              p.type === "image"
                ? "../media/posts/image/" + p.id + ".jpg"
                : "../media/posts/video/" + p.id + ".mp4",
            type: p.type,
            upVotes: p.upVotes,
            downVotes: p.downVotes,
            isUpVoted: p.isUpVoted,
            isDownVoted: p.isDownVoted,
          });
        });

        setPosts(postsData);
      }
    });
  }, []);

  return (
    <EmptyPage>
      <div className="main-container container-fluid">
        <div className="main-content row">
          <PostContainer posts={posts} />
        </div>
      </div>
    </EmptyPage>
  );
}

export default MainPage;
