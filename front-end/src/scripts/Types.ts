export type SearchResult = {
  username?: string;
  groupName?: string;
  postTitle?: string;
  profilePicture?: string;
  url: string;
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
  post,
  group,
  user,
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

  addGroup = "group/add",
  subscribeToGroup = "group/subscribe",
}
