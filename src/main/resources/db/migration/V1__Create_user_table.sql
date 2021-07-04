create table USER
(
	ID bigint auto_increment,
	ACCOUNT_ID varchar(100) null,
	NAME varchar(50) null,
	TOKEN character(36) null,
	GMT_CREATE bigint null,
	GMT_MODIFIED bigint null,
	bio varchar(256) null,
	avatar_url varchar(100) null,
	password varchar(20) null,
	constraint USER_pk
		primary key (ID)
);