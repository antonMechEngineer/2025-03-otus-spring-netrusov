insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(payload_comment, book_id)
values ('Comment_1_to_book_1', 1), ('Comment_2_to_book_1', 1), ('Comment_2_to_book_2', 2);

insert into users (user_name, password)
values ('abc', '$2a$12$i9Dekxpdi0fS1IyILNiEsOVewcLRXPajF6bX4JhuD2BasY5l00FLe');
