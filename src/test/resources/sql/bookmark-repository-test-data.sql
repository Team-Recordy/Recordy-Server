insert into `users` (`id`, `platform_id`, `platform_type`, `status`, `age_term`, `personal_info_term`, `use_term`, `nickname`)
values (1, 'abcdefg', 'KAKAO', 'ACTIVE', true, true, true, 'konu');

insert into `users` (`id`, `platform_id`, `platform_type`, `status`, `age_term`, `personal_info_term`, `use_term`, `nickname`)
values (2, 'abcdefgh', 'KAKAO', 'ACTIVE', true, true, true, 'subin');

insert into `users` (`id`, `platform_id`, `platform_type`, `status`, `age_term`, `personal_info_term`, `use_term`, `nickname`)
values (3, 'abcdefghi', 'KAKAO', 'ACTIVE', true, true, true, 'sebin');

insert into `locations` (`id`)
values (1);

insert into `places` (`id`, `name`, `location_id`)
values (1, 'place1', 1);

insert into `records` (`id`, `user_id`, `content`,`thumbnail_url`, `video_url`, `place_id`)
values (1, 1, 'content', 'thumbnail_url', 'video_url', 1);

insert into `records` (`id`, `user_id`, `content`,`thumbnail_url`, `video_url`, `place_id`)
values (2, 1, 'content', 'thumbnail_url', 'video_url', 1);

insert into `bookmarks` (`id`, `record_id`, `user_id`)
values(1, 1, 1);

insert into `bookmarks` (`id`, `record_id`, `user_id`)
values(2, 1, 2);

insert into `bookmarks` (`id`, `record_id`, `user_id`)
values(3, 2, 1);

insert into `bookmarks` (`id`, `record_id`, `user_id`)
values(4, 2, 2);