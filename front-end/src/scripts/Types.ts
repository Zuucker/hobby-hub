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
  id: number;
  content: string;
  groupName?: string;
  username?: string;
  postId?: number;
  type: NotificationType;
  likesAmmount?: number;
};

export enum NotificationType {
  ammount_of_likes = 0,
  group_join = 1,
  user_mention = 2,
  comment_reply = 3,
  registered = 4,
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
  getUserNotifications = "user/notifications",
  addNotification = "user/addNotification",

  addGroup = "group/add",
  subscribeToGroup = "group/subscribe",
  checkGroupNameAvailability = "group/isNameFree",
  getGroupData = "group/data",
  editGroup = "group/edit",
  leaveGroup = "group/leave",
  getGroupPosts = "group/posts",
  getTopGroups = "group/top",

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
