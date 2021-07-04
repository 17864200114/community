create table likerecord
(
	id int auto_increment,
	comment_id bigint null,
	liker_id int null,
	like_type int null,
	constraint likerecord_pk
		primary key (id)
);