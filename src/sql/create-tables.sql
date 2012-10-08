CREATE TABLE profiles
(
    id int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT,
    email varchar(45) NOT NULL,
    password varchar(45) NOT NULL,
    name varchar(45) NOT NULL
) DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX name ON profiles ( name );

CREATE TABLE projects
(
    id int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name varchar(45) NOT NULL,
    description longtext NOT NULL
) DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX name ON projects ( name );

CREATE TABLE tasks
(
    id int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT,
    description longtext,
    state char(11) DEFAULT 'NEW' NOT NULL,
    creator_id int unsigned NOT NULL,
    owner_id int unsigned NOT NULL,
    project_id int unsigned NOT NULL,
    FOREIGN KEY ( project_id ) REFERENCES projects ( id )
) DEFAULT CHARSET=utf8;

CREATE INDEX FK_tasks_users_2 ON tasks ( owner_id );
CREATE INDEX FK_tasks_users_1 ON tasks ( creator_id );
