CREATE TABLE `users` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `username` STRING NOT NULL,
  `email` STRING NOT NULL,
  `password` STRING NOT NULL,
  `verified` BOOL DEFAULT false,
  `jwt_token` STRING
);

CREATE TABLE `groups` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `author_id` INTEGER NOT NULL,
  `title` STRING NOT NULL,
  `description` STRING
);

CREATE TABLE `mods` (
  `user_id` INTEGER NOT NULL,
  `group_id` INTEGER NOT NULL
);

CREATE TABLE `posts` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `author_id` INTEGER NOT NULL,
  `group_id` INTEGER NOT NULL,
  `title` STRING,
  `type` INTEGER NOT NULL,
  `link` STRING,
  `up_votes` INTEGER,
  `down_votes` INTEGER
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
  `content` STRING NOT NULL
);

CREATE TABLE `subscriptions` (
  `user_id` INTEGER NOT NULL,
  `group_id` INTEGER NOT NULL
);

CREATE TABLE `liked_posts` (
  `user_id` INTEGER NOT NULL,
  `post_id` INTEGER NOT NULL
);

CREATE TABLE `verifications` (
  `id` INTEGER UNIQUE PRIMARY KEY,
  `user_id` INTEGER NOT NULL,
  `STRING` key NOT NULL
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
