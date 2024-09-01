insert into `users` (`id`, `platform_id`, `platform_type`, `status`, `age_term`, `personal_info_term`, `use_term`, `nickname`)
values (1, 'abcdefg', 'KAKAO', 'ACTIVE', true, true, true, 'konu');

insert into `users` (`id`, `platform_id`, `platform_type`, `status`, `age_term`, `personal_info_term`, `use_term`, `nickname`)
values (2, 'abcdefgh', 'KAKAO', 'ACTIVE', true, true, true, 'subin');

insert into `keywords` (`id`, `keyword`)
values (1, '감각적인');

insert into `keywords` (`id`, `keyword`)
values (2, '강렬한');

insert into `keywords` (`id`, `keyword`)
values (3, '귀여운');

insert into `records` (`id`, `user_id`, `content`, `location`,`thumbnail_url`, `video_url`)
values (1, 1, 'content', 'location', 'thumbnail_url', 'video_url');

insert into `records` (`id`, `user_id`, `content`, `location`,`thumbnail_url`, `video_url`)
values (2, 1, 'content', 'location', 'thumbnail_url', 'video_url');