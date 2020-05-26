# community
zzzzzzzzzzzzzzzzzzzzzzzzzzzzzqk


mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate


create table comment
(
	id bigint auto_increment,
	parent_id bigint not null,
	type int not null,
	commenter int not null,
	gmt_create bigint not null,
	gmt_modified bigint not null,
	like_count bigint default 0,
	content VARCHAR(1024) null,
	constraint comment_pk
		primary key (id)
);



             
