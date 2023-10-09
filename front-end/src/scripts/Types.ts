export type SearchResult = {
  username?: string;
  groupName?: string;
  postTitle?: string;
  profilePicture?: string;
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
  checkUsernameAvailability = "auth/usernameAvailability",
}
