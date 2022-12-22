CREATE TABLE IF NOT exists users (
    id bigint NOT NULL PRIMARY KEY,
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

ALTER SEQUENCE users_id_seq OWNED BY users.id;


ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--ALTER TABLE ONLY public.users
--    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);
--
--
--ALTER TABLE ONLY public.users
--    ADD CONSTRAINT uk_9hff2y8h354y83xh66c0lmspb UNIQUE (afm);
--
--
--ALTER TABLE ONLY public.users
--    ADD CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username);
--
--
--ALTER TABLE ONLY public.users
--    ADD CONSTRAINT users_pkey PRIMARY KEY (id);



--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

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

--
--ALTER TABLE ONLY public.roles
--    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);
--


CREATE TABLE IF NOT exists user_roles (
    user_id bigint NOT NULL,
    role_id integer NOT NULL,
    --CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES roles(id)
    --CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES users(id)
    CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users(id)
);


--CREATE TABLE IF NOT EXISTS authorities (
--    username varchar(50) NOT NULL,
--    authority varchar(50) NOT NULL,
--    unique(username,authority),
--    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username));

INSERT INTO roles(name) VALUES
('ROLE_TENANT'),	--id: 1
('ROLE_LESSOR'),	--id: 2
('ROLE_ADMIN');		--id: 3

INSERT INTO users (username, password, email) VALUES
('tenant', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', 'tenant@gmail.com'),	--1
('lessor', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', 'lessor@gmail.com'),	--2
('admin', '$2a$04$DR/f..s1siWJc8Xg3eJgpeB28a4V6kYpnkMPeOuq4rLQ42mJUYFGC', 'admin@gmail.com');	--3


INSERT INTO user_roles(user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 3);

