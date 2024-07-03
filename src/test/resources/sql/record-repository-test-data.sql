insert into `users` (`id`, `platform_id`, `platform_type`, `status`)
values (1, 'abcdefg', 'KAKAO', 'ACTIVE');

insert into `records` (`id`, `user_id`, `content`, `location`,`thumbnail_url`, `video_url`)
values (1, 1, 'content', 'location', 'thumbnail_url', 'video_url');