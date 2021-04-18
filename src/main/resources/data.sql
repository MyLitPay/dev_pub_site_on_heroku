drop table if exists captcha_codes CASCADE;
drop table if exists global_settings CASCADE;
drop table if exists post_comments CASCADE;
drop table if exists post_votes CASCADE;
drop table if exists posts CASCADE;
drop table if exists tag2post CASCADE;
drop table if exists tags CASCADE;
drop table if exists users CASCADE;

create table users (
    id INT not null auto_increment,
    code varchar(255),
    email varchar(255) not null,
    is_moderator tinyint not null,
    name varchar(255) not null,
    password varchar(255) not null,
    photo CLOB,
    reg_time TIMESTAMP not null,
    code_time TIMESTAMP,
    primary key (id)
);

create table posts (
    id INT not null auto_increment,
    is_active tinyint not null,
    moderation_status enum('NEW', 'ACCEPTED', 'DECLINED') not null default 'NEW',
    moderator_id INT,
    text CLOB not null,
    time TIMESTAMP not null,
    title varchar(255) not null,
    view_count INT not null,
    user_id INT not null,
    primary key (id)
);

create table post_votes (
    id INT not null auto_increment,
    time TIMESTAMP not null,
    value tinyint not null,
    post_id INT not null,
    user_id INT not null,
    primary key (id)
);

create table tags (
    id INT not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

create table tag2post (
    id INT not null auto_increment,
    post_id INT not null,
    tag_id INT not null,
    primary key (id)
);

create table post_comments (
    id INT not null auto_increment,
    parent_id INT,
    text CLOB not null,
    time TIMESTAMP not null,
    post_id INT not null,
    user_id INT not null,
    primary key (id)
);

create table captcha_codes (
    id INT not null auto_increment,
    code CLOB not null,
    secret_code CLOB not null,
    time TIMESTAMP not null,
    primary key (id)
);

create table global_settings (
    id INT not null auto_increment,
    code varchar(255) not null,
    name varchar(255) not null,
    value varchar(255) not null,
    primary key (id)
);

alter table post_comments
    add constraint FKaawaqxjs3br8dw5v90w7uu514
    foreign key (post_id) references posts (id);

alter table post_comments
    add constraint FKsnxoecngu89u3fh4wdrgf0f2g
    foreign key (user_id) references users (id);

alter table post_votes
    add constraint FK9jh5u17tmu1g7xnlxa77ilo3u
    foreign key (post_id) references posts (id);

alter table post_votes
    add constraint FK9q09ho9p8fmo6rcysnci8rocc
    foreign key (user_id) references users (id);

alter table posts
    add constraint FK5lidm6cqbc7u4xhqpxm898qme
    foreign key (user_id) references users (id);

alter table tag2post
    add constraint FKpjoedhh4h917xf25el3odq20i
    foreign key (post_id) references posts (id);

alter table tag2post
    add constraint FKjou6suf2w810t2u3l96uasw3r
    foreign key (tag_id) references tags (id);

insert into global_settings (code, name, value)
values ('MULTIUSER_MODE', 'Многопользовательский режим', 'YES');

insert into global_settings (code, name, value)
values ('POST_PREMODERATION', 'Премодерация постов', 'YES');

insert into global_settings (code, name, value)
values ('STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'NO');

insert into users (code, email, is_moderator, name, password, photo, reg_time)
values (null, 'moder@m.com', 1, 'Moderator', '$2a$12$l1NXbHE0C0MKEC/frhN59O4V.E7JaSSMvVdVz54WIAZCiKkCz2YCy',
'/upload/AeA/Cjm/WQt/moder.jpeg', '2021-03-16 23:15:57');

insert into users (code, email, is_moderator, name, password, photo, reg_time)
values ('0', 'fredo@m.com', 0, 'Fredo', '$2a$12$t3lWDTt9xRGbtHzOFdp0YeUWU8Uhzdqc2MsD6H1dSQiVkc7sABANG',
'/upload/EVn/RjG/wVS/Panda.jpeg', '2021-03-19 19:10:41');

insert into users (code, email, is_moderator, name, password, photo, reg_time)
values ('d3e9995119da44cdb2913fb138d8437a', 'willi@m.com', 0, 'William', '$2a$12$x6l2rUA2/12SgJtZgkElMuMWMn6Q1DoDoIfQu3R..dO1NsdZyi3aC',
'/upload/fgb/MOC/Zij/fox.jpeg', '2021-03-31 22:23:29');

insert into posts (is_active, moderation_status, moderator_id, text, time, title, view_count, user_id)
values (1, 'NEW', null, '<span style="color: rgb(51, 51, 51); font-family: -apple-system, system-ui, &quot;Segoe UI&quot;, Arial, sans-serif; font-size: 16px; background-color: rgb(255, 255, 255);">Компьютерное зрение — направление в области анализа данных. Системы, которые оснащаются этой технологией, могут отвечать за очень важные процессы. Для примера можно взять автомобиль Tesla, "Yandex Self-Driving Car", медицинские системы анализов, видеокамеры и т.д. Подобные системы должны проектироваться с заложенными в них системами защиты, причем эти системы не должны защищать только от «типичных» угроз для информационных систем вроде эксплойтов, вредоносного ПО или Ddos атак.</span>',
'2021-04-01 20:22:17', 'Атаки на компьютерное зрение',
0, 2);

insert into posts (is_active, moderation_status, moderator_id, text, time, title, view_count, user_id)
values (1, 'ACCEPTED', 1, '<span style="color: rgb(51, 51, 51); font-family: -apple-system, system-ui, &quot;Segoe UI&quot;, Arial, sans-serif; font-size: 16px; background-color: rgb(255, 255, 255);">Двумерные функции принимают два входных значения (x и y) и выводят единожды вычисленное на основе входа значение. Эти функции — одни из самых простых для изучения оптимизации. Их преимущество в том, что они могут визуализироваться в виде контурного графика или графика поверхности, показывающего топографию проблемной области с оптимумом и уникальными элементами, которые отмечены точками. В этом туториале вы ознакомитесь со стандартными двумерными тестовыми функциями, которые можно использовать при изучении оптимизации функций. Давайте начнём.<img src="/upload/FaX/eWl/Txv/No_face.jpeg"></span>',
'2021-04-06 21:22:00', 'Двумерные тестовые функции для оптимизации',
11, 3);

insert into post_votes (time, value, post_id, user_id)
values ('2021-04-07 21:22:00', 1, 2, 1);

insert into post_votes (time, value, post_id, user_id)
values ('2021-04-07 21:22:00', -1, 2, 2);

insert into tags (name) values ('Maths');
insert into tags (name) values ('AI');
insert into tags (name) values ('Task');
insert into tags (name) values ('Robots');
insert into tags (name) values ('Physics');

insert into tag2post (post_id, tag_id) values (1, 1);
insert into tag2post (post_id, tag_id) values (1, 2);
insert into tag2post (post_id, tag_id) values (1, 3);
insert into tag2post (post_id, tag_id) values (2, 4);
insert into tag2post (post_id, tag_id) values (2, 5);
insert into tag2post (post_id, tag_id) values (2, 1);

insert into post_comments (parent_id, text, time, post_id, user_id)
values (null, 'Nice post', '2021-04-08 21:22:00', 2, 2);

insert into post_comments (parent_id, text, time, post_id, user_id)
values (1, 'Thank you', '2021-04-08 23:22:00', 2, 3);

insert into post_comments (parent_id, text, time, post_id, user_id)
values (null, 'Some comment', '2021-04-09 21:22:00', 2, 1);

insert into captcha_codes (code, secret_code, time)
values ('ahapikiyo', '40b35ec5-a4b2-457a-8d8e-06958a1c13b1', '2021-03-15 13:58:25');

