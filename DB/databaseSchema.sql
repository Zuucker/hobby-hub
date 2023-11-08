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
  `author_id` INTEGER NOT NULL,
  `title` STRING NOT NULL,
  `description` STRING,
  FOREIGN KEY (author_id) REFERENCES `users`(id)
);

CREATE TABLE `mods` (
  `user_id` INTEGER NOT NULL,
  `group_id` INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `users`(id),
  FOREIGN KEY (group_id) REFERENCES `groups`(id)
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
  FOREIGN KEY (author_id) REFERENCES `users`(id),
  FOREIGN KEY (group_id) REFERENCES `groups`(id),
  FOREIGN KEY (type) REFERENCES `types`(id)
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
  FOREIGN KEY (post_id) REFERENCES referenced_table(referenced_column),
  FOREIGN KEY (comment_id) REFERENCES referenced_table(referenced_column)
);

CREATE TABLE `subscriptions` (
  `user_id` INTEGER NOT NULL,
  `group_id` INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `users`(id),
  FOREIGN KEY (group_id) REFERENCES `groups`(id)
);

CREATE TABLE `liked_posts` (
  `user_id` INTEGER NOT NULL,
  `post_id` INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `users`(id),
  FOREIGN KEY (post_id) REFERENCES `posts`(id)
);

CREATE TABLE `verifications` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `user_id` INTEGER NOT NULL,
  `code` STRING NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `users`(id)
);

CREATE TABLE `notifications` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `user_id` INTEGER NOT NULL,
  `content` STRING NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `users`(id)
);