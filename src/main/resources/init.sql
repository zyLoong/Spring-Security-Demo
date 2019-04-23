create schema securityTest collate utf8_general_ci;

create table user
(
    id int auto_increment
        primary key,
    username varchar(36) not null ,
    password varchar(40) not null ,
    status tinyint(1) not null ,
    createtime timestamp default CURRENT_TIMESTAMP not null
#     login varchar(40) null
);

create table user_role
(
    user_id int not null,
    role_id int not null,
    primary key (role_id, user_id)
);

create table role
(
    id tinyint auto_increment comment '主键'
        primary key,
    role varchar(36) not null ,
    description varchar(256) null comment '角色描述'
);


create table role_permission
(
    role_id int not null,
    permission_id int not null,
    primary key (role_id, permission_id)
);

create table permission
(
    id int auto_increment
        primary key,
    description varchar(40) null,
    prefix varchar(256) null,
    method varchar(32) null,
    url varchar(128) null
);

# 密码为 123 ，数据库保存的是 sha1 后的
insert into user (id, username, password , status) values (1, 'admin', '40bd001563085fc35165329ea1ff5c5ecbdbbeef', true);
insert into user (id, username, password , status) values (2, 'user', '40bd001563085fc35165329ea1ff5c5ecbdbbeef', true);

insert into role (id, role, description) value (1,'admin','管理员角色');
insert into role (id, role, description) value (2,'user','普通用户角色');

insert into user_role (user_id, role_id) value (1, 1);
insert into user_role (user_id, role_id) value (2, 2);


insert into permission(id, description, prefix, method, url) values (1,'admin接口','/test','POST','/admin');
insert into role_permission(role_id, permission_id) value (1, 1);

insert into permission(id, description, prefix, method, url) values (2,'use接口','/test','GET','/user');
insert into role_permission(role_id, permission_id) value (1, 2);
insert into role_permission(role_id, permission_id) value (2, 2);
