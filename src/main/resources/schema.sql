create table IF NOT EXISTS GENRES
(
    ID   INTEGER auto_increment
        primary key,
    NAME CHARACTER VARYING
);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  BIGINT  not null
        primary key,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRE_TO_GENRE
        foreign key (GENRE_ID) references GENRES
);

create table IF NOT EXISTS MPA
(
    ID   INTEGER auto_increment
        primary key,
    NAME CHARACTER VARYING
);

create table IF NOT EXISTS FILMS
(
    NAME         CHARACTER VARYING not null,
    ID           BIGINT auto_increment
        primary key,
    RELEASE_DATE TIMESTAMP,
    DESCRIPTION  CHARACTER VARYING(200),
    MPA          INTEGER,
    DURATION     INTEGER,
    RATE         INTEGER,
    constraint FILMS_TO_MPA
        foreign key (MPA) references MPA,
    CONSTRAINT name_check check (NAME not like ''),
    CONSTRAINT release_date_check check (CAST(RELEASE_DATE as date) >= '1895-12-28'),
    CONSTRAINT duration_check check (DURATION > 0)
);

create table IF NOT EXISTS USERS
(
    ID       INTEGER auto_increment
        primary key,
    NAME     CHARACTER VARYING,
    LOGIN    CHARACTER VARYING not null,
    EMAIL    CHARACTER VARYING not null,
    BIRTHDAY TIMESTAMP,
    CONSTRAINT login_check check (LOGIN not like '% %' and LOGIN not like ''),
    CONSTRAINT email_check check (EMAIL like '%@%')
);

create table IF NOT EXISTS FRIENDS
(
    FRIEND_ID INTEGER not null,
    USER_ID   INTEGER not null,
    primary key (FRIEND_ID, USER_ID),
    constraint FRIEND_TO_USER
        foreign key (FRIEND_ID) references USERS,
    constraint USER_TO_USER
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS LIKES
(
    USER_ID INTEGER not null,
    FILM_ID INTEGER not null
        primary key,
    constraint LIKES_TO_FILMS
        foreign key (FILM_ID) references FILMS,
    constraint LIKES_TO_USERS
        foreign key (USER_ID) references USERS
);




