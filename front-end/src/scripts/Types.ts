export type SearchResult = {
  username?: string;
  groupName?: string;
  postTitle?: string;
  profilePicture?: string;
  url: string;
  type: SearchResultsType;
  postId?: number;
};

export type Notification = {
  type: NotificationType;
  url: string;
  groupName?: string;
  username?: string;
  likesAmmount?: number;
};

export enum NotificationType {
  ammount_of_likes,
  group_join,
  user_mention,
  comment_reply,
}

export enum SearchResultsType {
  post = "post",
  group = "group",
  user = "user",
}

export type Post = {
  id: number;
  title: string;
  authorId: number;
  author?: string;
  groupId: number;
  group?: string;
  type: string;
  link: string;
  upVotes: number;
  downVotes: number;
  isUpVoted: boolean;
  isDownVoted: boolean;
};

export type Comment = {
  id: number;
  authorId: number;
  author: string;
  postId: number;
  content: string;
  points: number;
  interacted: boolean;
  upvoted: boolean;
  subcomments?: Comment[];
};

export type GroupData = {
  name: string;
  description: string;
  ownerName: string;
  id: number;
  ownerId: number;
};

export enum SearchTypeEnum {
  U,
  G,
  P,
  UG,
  UGP,
  UP,
  GP,
}

export enum Endpoints {
  registerUser = "auth/register",
  loginUser = "auth/login",
  checkUsernameAvailability = "auth/usernameAvailability",
  verifyUser = "auth/verify",

  getUserData = "user/data",
  getUserDataFromJwt = "user/data2",
  editUser = "user/edit",
  getUsernameFromJwt = "user/getUsernameFromJwt",
  getUserGroups = "user/getUserGroups",
  getUserPosts = "user/posts",
  getUserFeed = "user/feed",
  hasJoinedGroup = "user/hasJoinedGroup",

  addGroup = "group/add",
  subscribeToGroup = "group/subscribe",
  checkGroupNameAvailability = "group/isNameFree",
  getGroupData = "group/data",
  editGroup = "group/edit",
  leaveGroup = "group/leave",
  getGroupPosts = "group/posts",

  addPost = "post/add",
  likePost = "post/like",
  dislikePost = "post/dislike",
  unLikePost = "post/unLike",
  unDislikePost = "post/unDislike",
  addComment = "post/addComment",
  interactWithComment = "post/interactWithCommentPoint",
  getPostComments = "post/comments",
  getPostData = "post/data",

  search = "search/data",
}
