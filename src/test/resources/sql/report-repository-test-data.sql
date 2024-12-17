insert into `users` (`id`, `platform_id`, `platform_type`, `status`, `age_term`, `personal_info_term`, `use_term`, `nickname`)
values (1, 'abcdefg', 'KAKAO', 'ACTIVE', true, true, true, 'konu');

insert into `users` (`id`, `platform_id`, `platform_type`, `status`, `age_term`, `personal_info_term`, `use_term`, `nickname`)
values (2, 'abcdefgh', 'KAKAO', 'ACTIVE', true, true, true, 'subin');

insert into `locations` (`id`)
values (1);

insert into `places` (`id`, `name`, `location_id`, `platform_id`)
values (1, 'place1', 1, UUID());

insert into `records` (`id`, `user_id`, `content`,`thumbnail_url`, `video_url`, `place_id`, `is_blocked`)
values (1, 1, 'content', 'thumbnail_url', 'video_url', 1, false);

insert into `records` (`id`, `user_id`, `content`,`thumbnail_url`, `video_url`, `place_id`, `is_blocked`)
values (2, 1, 'content', 'thumbnail_url', 'video_url', 1, false);

insert into `reports` (`id`, `user_id`, `record_id`, `reason`, `content`, `approval_status`, `created_at`)
values (1, 1, 2, 'OTHER', '신고 내용', 'PENDING', NOW());

insert into `reports` (`id`, `user_id`, `record_id`, `reason`, `content`, `approval_status`,`created_at`)
values (2, 2, 2, 'OTHER', '신고 내용', 'PENDING', NOW());