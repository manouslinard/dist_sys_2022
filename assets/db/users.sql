
CREATE TABLE IF NOT exists users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) not null,
    enabled boolean not null
    );


CREATE TABLE IF NOT EXISTS authorities (
    id integer NOT NULL PRIMARY KEY,
	username varchar(50) NOT NULL,
    authority varchar(50) NOT NULL,
    unique(username,authority),
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username));

CREATE SEQUENCE IF NOT EXISTS auth_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE auth_id_seq OWNED BY authorities.id;


ALTER TABLE ONLY authorities ALTER COLUMN id SET DEFAULT nextval('auth_id_seq'::regclass);

    
    
INSERT INTO users (username, password, enabled) VALUES
('tenant', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', 't'),
('lessor', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', 't'),
('admin', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', 't');


INSERT INTO authorities(username, authority) VALUES
('admin', 'ROLE_ADMIN'),
('tenant', 'ROLE_TENANT'),
('lessor', 'ROLE_LESSOR');


-- BACKEND:


CREATE TABLE IF NOT exists avg_users (
    id integer NOT NULL PRIMARY KEY,
    afm character varying(255) UNIQUE,
    email character varying(50) UNIQUE,
    first_name character varying(30),
    last_name character varying(30),
    password character varying(120),
    phone character varying(255),
    username character varying(20) UNIQUE
    
);


CREATE SEQUENCE IF NOT EXISTS users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE users_id_seq OWNED BY avg_users.id;


ALTER TABLE ONLY avg_users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


CREATE TABLE IF NOT EXISTS roles (
    id integer NOT NULL PRIMARY KEY,
    name character varying(20)
);


CREATE SEQUENCE IF NOT EXISTS roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE roles_id_seq OWNED BY roles.id;


ALTER TABLE ONLY roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq'::regclass);


CREATE TABLE IF NOT exists user_roles (
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES avg_users(id)
);

INSERT INTO roles(name) VALUES
('ROLE_TENANT'),	--id: 1
('ROLE_LESSOR'),	--id: 2
('ROLE_ADMIN');		--id: 3

INSERT INTO avg_users (username, password, email) VALUES
('tenant', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', 'tenant@gmail.com'),	--1
('lessor', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', 'lessor@gmail.com'),	--2
('admin', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', 'admin@gmail.com');	--3


INSERT INTO user_roles(user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 3);




