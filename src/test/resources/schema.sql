--
-- PostgreSQL database dump
--

-- Dumped from database version 11.0 (Debian 11.0-1.pgdg90+2)
-- Dumped by pg_dump version 11.1

-- Started on 2019-08-30 15:56:13 CEST

--
-- TOC entry 197 (class 1259 OID 24577)
-- Name: permissions; Type: TABLE; Schema: public; Owner: projectglobe
--

CREATE TABLE public.permissions (
    uuid character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    creator_session_uuid character varying(255),
    deleted_at timestamp without time zone,
    origin_reference character varying(255),
    updated_at timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    name character varying(255) NOT NULL
);

--
-- TOC entry 198 (class 1259 OID 24585)
-- Name: roles; Type: TABLE; Schema: public; Owner: projectglobe
--

CREATE TABLE public.roles (
    uuid character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    creator_session_uuid character varying(255),
    deleted_at timestamp without time zone,
    origin_reference character varying(255),
    updated_at timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    name character varying(255) NOT NULL
);

--
-- TOC entry 199 (class 1259 OID 24593)
-- Name: roles_privileges; Type: TABLE; Schema: public; Owner: projectglobe
--

CREATE TABLE public.roles_privileges (
    role_uuid character varying(255) NOT NULL,
    permission_uuid character varying(255) NOT NULL
);

--
-- TOC entry 196 (class 1259 OID 16385)
-- Name: users; Type: TABLE; Schema: public; Owner: projectglobe
--

CREATE TABLE public.users (
    uuid character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    creator_session_uuid character varying(255),
    deleted_at timestamp without time zone,
    origin_reference character varying(255),
    updated_at timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    email character varying(255) NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    password character varying(255) NOT NULL
);

--
-- TOC entry 200 (class 1259 OID 24599)
-- Name: users_roles; Type: TABLE; Schema: public; Owner: projectglobe
--

CREATE TABLE public.users_roles (
    user_uuid character varying(255) NOT NULL,
    role_uuid character varying(255) NOT NULL
);

--
-- TOC entry 2891 (class 0 OID 24577)
-- Dependencies: 197
-- Data for Name: permissions; Type: TABLE DATA; Schema: public; Owner: projectglobe
--

INSERT INTO public.permissions (uuid, created_at, creator_session_uuid, deleted_at, origin_reference, updated_at, version, name) VALUES ('627c0bc8-ff09-4533-a342-4ea4bbcd99a5', '2019-08-27 17:30:49.644', NULL, NULL, NULL, '2019-08-27 17:30:49.644', 0, 'USERS_READ_ALL');
INSERT INTO public.permissions (uuid, created_at, creator_session_uuid, deleted_at, origin_reference, updated_at, version, name) VALUES ('38ff7ca8-b8db-4828-8338-aecf8cc4f777', '2019-08-27 17:30:49.644', NULL, NULL, NULL, '2019-08-27 17:30:49.644', 0, 'USERS_READ_SELF');
INSERT INTO public.permissions (uuid, created_at, creator_session_uuid, deleted_at, origin_reference, updated_at, version, name) VALUES ('ea22210e-961b-4b50-8ddd-e8b03b1b3417', '2019-08-27 17:30:49.644', NULL, NULL, NULL, '2019-08-27 17:30:49.644', 0, 'USERS_CREATE');
INSERT INTO public.permissions (uuid, created_at, creator_session_uuid, deleted_at, origin_reference, updated_at, version, name) VALUES ('7c0cf2df-38ac-4c9d-b411-89233aa9945c', '2019-08-27 17:30:49.644', NULL, NULL, NULL, '2019-08-27 17:30:49.644', 0, 'USERS_UPDATE');
INSERT INTO public.permissions (uuid, created_at, creator_session_uuid, deleted_at, origin_reference, updated_at, version, name) VALUES ('157ad7d7-99db-47d3-b0e3-510a47af5866', '2019-08-27 17:30:49.644', NULL, NULL, NULL, '2019-08-27 17:30:49.644', 0, 'USERS_DELETE');



--
-- TOC entry 2892 (class 0 OID 24585)
-- Dependencies: 198
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: projectglobe
--

INSERT INTO public.roles (uuid, created_at, creator_session_uuid, deleted_at, origin_reference, updated_at, version, name) VALUES ('ROLE_ADMIN', '2019-08-27 17:30:49.644', NULL, NULL, NULL, '2019-08-27 17:30:49.644', 0, 'ROLE_ADMIN');
INSERT INTO public.roles (uuid, created_at, creator_session_uuid, deleted_at, origin_reference, updated_at, version, name) VALUES ('ROLE_USER', '2019-08-27 17:30:49.644', NULL, NULL, NULL, '2019-08-27 17:30:49.644', 0, 'ROLE_USER');


--
-- TOC entry 2893 (class 0 OID 24593)
-- Dependencies: 199
-- Data for Name: roles_privileges; Type: TABLE DATA; Schema: public; Owner: projectglobe
--

INSERT INTO public.roles_privileges (role_uuid, permission_uuid) VALUES ('ROLE_ADMIN', '627c0bc8-ff09-4533-a342-4ea4bbcd99a5');
INSERT INTO public.roles_privileges (role_uuid, permission_uuid) VALUES ('ROLE_ADMIN', 'ea22210e-961b-4b50-8ddd-e8b03b1b3417');
INSERT INTO public.roles_privileges (role_uuid, permission_uuid) VALUES ('ROLE_ADMIN', '7c0cf2df-38ac-4c9d-b411-89233aa9945c');
INSERT INTO public.roles_privileges (role_uuid, permission_uuid) VALUES ('ROLE_ADMIN', '157ad7d7-99db-47d3-b0e3-510a47af5866');


--
-- TOC entry 2890 (class 0 OID 16385)
-- Dependencies: 196
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: projectglobe
--

INSERT INTO public.users (uuid, created_at, creator_session_uuid, deleted_at, origin_reference, updated_at, version, email, first_name, last_name, password) VALUES ('f95143b2-7ba4-44ce-bcdd-c35ca00c1217', '2019-08-27 17:30:49.644', NULL, NULL, NULL, '2019-08-27 17:30:49.644', 0, 'a.anzilotti@hastega.it', 'Andrea', 'Anzilotti', '$2a$10$8vl/RUkPRfTglNWXya7xeerFstPg4MCvxnx6CEBEsG00Bsfpmkb6K');


--
-- TOC entry 2894 (class 0 OID 24599)
-- Dependencies: 200
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: projectglobe
--

INSERT INTO public.users_roles (user_uuid, role_uuid) VALUES ('f95143b2-7ba4-44ce-bcdd-c35ca00c1217', 'ROLE_ADMIN');


--
-- TOC entry 2762 (class 2606 OID 24584)
-- Name: permissions permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: projectglobe
--

ALTER TABLE public.permissions
    ADD PRIMARY KEY (uuid);


--
-- TOC entry 2764 (class 2606 OID 24592)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: projectglobe
--

ALTER TABLE public.roles
    ADD PRIMARY KEY (uuid);


--
-- TOC entry 2760 (class 2606 OID 16392)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: projectglobe
--

ALTER TABLE public.users
    ADD PRIMARY KEY (uuid);


--
-- TOC entry 2767 (class 2606 OID 24615)
-- Name: users_roles fkbg6u3r2t69cbo37bt4j7twyyq; Type: FK CONSTRAINT; Schema: public; Owner: projectglobe
--

ALTER TABLE public.users_roles
    ADD FOREIGN KEY (role_uuid) REFERENCES public.roles(uuid);


--
-- TOC entry 2768 (class 2606 OID 24620)
-- Name: users_roles fkgt7jn41bq2j481eor4ikmlvsd; Type: FK CONSTRAINT; Schema: public; Owner: projectglobe
--

ALTER TABLE public.users_roles
    ADD FOREIGN KEY (user_uuid) REFERENCES public.users(uuid);


--
-- TOC entry 2765 (class 2606 OID 24605)
-- Name: roles_privileges fkn5pv9x23gj0cn3ffsy7o5bfp3; Type: FK CONSTRAINT; Schema: public; Owner: projectglobe
--

ALTER TABLE public.roles_privileges
    ADD FOREIGN KEY (permission_uuid) REFERENCES public.permissions(uuid);


--
-- TOC entry 2766 (class 2606 OID 24610)
-- Name: roles_privileges fkpdrrqets5t7i4sqivjo7urq97; Type: FK CONSTRAINT; Schema: public; Owner: projectglobe
--

ALTER TABLE public.roles_privileges
    ADD FOREIGN KEY (role_uuid) REFERENCES public.roles(uuid);


-- Completed on 2019-08-30 15:56:13 CEST

--
-- PostgreSQL database dump complete
--

