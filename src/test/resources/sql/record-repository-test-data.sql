insert into `users` (`id`, `platform_id`, `platform_type`, `status`, `age_term`, `personal_info_term`, `use_term`, `nickname`)
values (1, 'abcdefg', 'KAKAO', 'ACTIVE', true, true, true, 'konu');

insert into `keywords` (`id`, `keyword`)
values (1, 'EXOTIC');

insert into `keywords` (`id`, `keyword`)
values (2, 'QUITE');

insert into `keywords` (`id`, `keyword`)
values (3, 'TRENDY');

insert into `records` (`id`, `user_id`, `content`, `location`,`thumbnail_url`, `video_url`)
values (1, 1, 'content', 'location', 'thumbnail_url', 'video_url');

insert into `records` (`id`, `user_id`, `content`, `location`,`thumbnail_url`, `video_url`)
values (2, 1, 'content', 'location', 'thumbnail_url', 'video_url');

insert into `records` (`id`, `user_id`, `content`, `location`,`thumbnail_url`, `video_url`)
values (3, 2, 'content', 'location', 'thumbnail_url', 'video_url');

insert into `records` (`id`, `user_id`, `content`, `location`,`thumbnail_url`, `video_url`)
values (4, 2, 'content', 'location', 'thumbnail_url', 'video_url');

insert into `records` (`id`, `user_id`, `content`, `location`,`thumbnail_url`, `video_url`)
values (5, 1, 'content', 'location', 'thumbnail_url', 'video_url');

insert into `uploads` (`id`, `record_id`, `keyword_id`)
values(1, 1, 1);

insert into `uploads` (`id`, `record_id`, `keyword_id`)
values(2, 2, 2);

insert into `uploads` (`id`, `record_id`, `keyword_id`)
values(3, 3, 1);

insert into `uploads` (`id`, `record_id`, `keyword_id`)
values(4, 4, 2);

insert into `uploads` (`id`, `record_id`, `keyword_id`)
values(5, 5, 1);