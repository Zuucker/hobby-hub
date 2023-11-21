CREATE TABLE `users` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `username` STRING NOT NULL,
  `email` STRING NOT NULL,
  `password` STRING NOT NULL,
  `verified` BOOL DEFAULT false,
  `register_date` STRING NOT NULL,
  `bio` STRING
);

CREATE TABLE `groups` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `owner_id` INTEGER NOT NULL,
  `name` STRING NOT NULL,
  `description` STRING,
  FOREIGN KEY (owner_id) REFERENCES `users`(id) ON DELETE CASCADE
);

CREATE TABLE `mods` (
  `user_id` INTEGER NOT NULL,
  `group_id` INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `users`(id) ON DELETE CASCADE,
  FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE CASCADE
);

CREATE TABLE `posts` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `author_id` INTEGER NOT NULL,
  `group_id` INTEGER NOT NULL,
  `title` STRING,
  `type` INTEGER NOT NULL,
  `link` STRING,
  `up_votes` INTEGER,
  `down_votes` INTEGER,
  FOREIGN KEY (author_id) REFERENCES `users`(id) ON DELETE CASCADE,
  FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE CASCADE,
  FOREIGN KEY (type) REFERENCES `types`(id) ON DELETE CASCADE
);

CREATE TABLE `types` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `type` STRING
);

CREATE TABLE `comments` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `author_id` INTEGER NOT NULL,
  `post_id` INTEGER,
  `comment_id` INTEGER,
  `content` STRING NOT NULL,
  FOREIGN KEY (author_id) REFERENCES `users`(id),
  FOREIGN KEY (post_id) REFERENCES referenced_table(referenced_column) ON DELETE CASCADE,
  FOREIGN KEY (comment_id) REFERENCES referenced_table(referenced_column) ON DELETE CASCADE
);

CREATE TABLE `group_subscriptions` (
  `user_id` INTEGER NOT NULL,
  `group_id` INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `users`(id) ON DELETE CASCADE,
  FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE CASCADE
);

CREATE TABLE `liked_posts` (
  `user_id` INTEGER NOT NULL,
  `post_id` INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `users`(id) ON DELETE CASCADE,
  FOREIGN KEY (post_id) REFERENCES `posts`(id) ON DELETE CASCADE
);

CREATE TABLE `verifications` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `user_id` INTEGER NOT NULL,
  `code` STRING NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `users`(id) ON DELETE CASCADE
);

CREATE TABLE `notifications` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `user_id` INTEGER NOT NULL,
  `content` STRING NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `users`(id) ON DELETE CASCADE
);

PRAGMA foreign_keys = ON;