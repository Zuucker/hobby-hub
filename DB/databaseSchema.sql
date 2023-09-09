CREATE TABLE `users` (
  `id` int UNIQUE PRIMARY KEY,
  `username` string NOT NULL,
  `email` string NOT NULL,
  `password` string NOT NULL,
  `verified` bool DEFAULT false,
  `jwt_token` string
);

CREATE TABLE `groups` (
  `id` int UNIQUE PRIMARY KEY,
  `author_id` int NOT NULL,
  `title` string NOT NULL,
  `description` string
);

CREATE TABLE `mods` (
  `user_id` int NOT NULL,
  `group_id` int NOT NULL
);

CREATE TABLE `posts` (
  `id` int UNIQUE PRIMARY KEY,
  `author_id` int NOT NULL,
  `group_id` int NOT NULL,
  `title` string,
  `type` int NOT NULL,
  `link` string,
  `up_votes` int,
  `down_votes` int
);

CREATE TABLE `types` (
  `id` int UNIQUE PRIMARY KEY,
  `type` string
);

CREATE TABLE `comments` (
  `id` int UNIQUE PRIMARY KEY,
  `author_id` int NOT NULL,
  `post_id` int,
  `comment_id` int,
  `content` string NOT NULL
);

CREATE TABLE `subscriptions` (
  `user_id` int NOT NULL,
  `group_id` int NOT NULL
);

CREATE TABLE `liked_posts` (
  `user_id` int NOT NULL,
  `post_id` int NOT NULL
);

CREATE TABLE `verifications` (
  `id` int UNIQUE PRIMARY KEY,
  `user_id` int NOT NULL,
  `string` key NOT NULL
);

ALTER TABLE `groups` ADD FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);

ALTER TABLE `mods` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `mods` ADD FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`);

ALTER TABLE `posts` ADD FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);

ALTER TABLE `posts` ADD FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`);

ALTER TABLE `posts` ADD FOREIGN KEY (`type`) REFERENCES `types` (`id`);

ALTER TABLE `comments` ADD FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);

ALTER TABLE `comments` ADD FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

ALTER TABLE `comments` ADD FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`);

ALTER TABLE `subscriptions` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `subscriptions` ADD FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`);

ALTER TABLE `liked_posts` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `liked_posts` ADD FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

ALTER TABLE `verifications` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
