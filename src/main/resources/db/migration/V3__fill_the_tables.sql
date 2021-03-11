insert into users (code, email, is_moderator, name, password, photo, reg_time)
values ('0', 'mike@gmail.com', 1, 'Mike', '123', '0', '2019-01-21 11:10:10');

insert into users (code, email, is_moderator, name, password, photo, reg_time)
values ('0', 'tom@gmail.com', 0, 'Tom', '456', '0', '2020-02-12 12:12:12');

insert into users (code, email, is_moderator, name, password, photo, reg_time)
values ('0', 'bill@gmail.com', 0, 'Bill', '789', '0', '2020-02-15 13:15:10');

insert into posts (is_active, moderation_status, moderator_id, text, time, title, view_count, user_id)
values (1, 'ACCEPTED', 1, 'Два исследователя Facebook из Парижа создали для FB новую нейронную сеть, способную решать сложные математические уравнения, даже те, которые имеют дело с математическим анализом. Их работа описана в статье от 2 декабря, опубликованной в архиве arXiv (хранилище научных исследований под управлением Корнельского университета). Это ещё один большой шаг вперёд для нейронных сетей.',
'2021-03-01 14:01:01', 'Как новая нейронная сеть Facebook решает дифференциальные уравнения',
15, 2);

insert into posts (is_active, moderation_status, moderator_id, text, time, title, view_count, user_id)
values (0, 'NEW', 1, 'Доцент кафедры информатики в Копенгагенском университете Джейкоб Холм просматривал доказательства из научной статьи, опубликованной в Интернете в октябре 2019 года им и его коллегой Евой Ротенберг (доцентом кафедры прикладной математики и информатики Датского технического университета), и обнаружил, что их результаты невольно дали решение многовековой проблемы графов.',
'2021-03-10 11:02:02', 'Если нарисуем такие графы, сможем навсегда изменить компьютеры',
0, 3);

insert into posts (is_active, moderation_status, moderator_id, text, time, title, view_count, user_id)
values (1, 'ACCEPTED', 1, 'На японском пирсе Ямасита, примерно в 40 километрах к югу от Токио, появился самый большой в мире человекоподобный робот. Он создан по образцу RX-78-2 Gundam, вымышленного робота, который с 1979 года был предметом примерно 50 одноимённых телесериалов и манги. У этого гиганта высотой почти 20 метров 24 степени свободы. Это означает, что он может двигаться в любом направлении.',
'2021-03-04 15:03:03', 'Хотите шагающего 18-метрового робота? Измените законы физики',
10, 3);

insert into post_votes (time, value, post_id, user_id)
values ('2021-03-01 16:01:01', 1, 1, 3);

insert into post_votes (time, value, post_id, user_id)
values ('2021-03-01 17:01:01', 1, 1, 2);

insert into post_votes (time, value, post_id, user_id)
values ('2021-03-05 12:23:12', 1, 3, 3);

insert into post_votes (time, value, post_id, user_id)
values ('2021-03-05 14:25:15', -1, 3, 2);

insert into tags (name) values ('Maths');
insert into tags (name) values ('AI');
insert into tags (name) values ('Interesting task');
insert into tags (name) values ('Robots');
insert into tags (name) values ('Physics');

insert into tag2post (post_id, tag_id) values (1, 1);
insert into tag2post (post_id, tag_id) values (1, 2);
insert into tag2post (post_id, tag_id) values (2, 1);
insert into tag2post (post_id, tag_id) values (2, 3);
insert into tag2post (post_id, tag_id) values (3, 4);
insert into tag2post (post_id, tag_id) values (3, 5);

insert into post_comments (parent_id, text, time, post_id, user_id)
values (null, 'First comment', '2021-03-02 14:01:01', 1, 3);

insert into post_comments (parent_id, text, time, post_id, user_id)
values (1, 'Answer on first comment', '2021-03-02 18:02:02', 1, 2);

insert into post_comments (parent_id, text, time, post_id, user_id)
values (null, 'First comment', '2021-03-05 15:03:03', 3, 2);

insert into post_comments (parent_id, text, time, post_id, user_id)
values (null, 'Second comment', '2021-03-05 18:14:10', 3, 2);

