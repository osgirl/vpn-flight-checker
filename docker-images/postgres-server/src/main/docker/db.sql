--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: postgres; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: flight_query; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE flight_query (
    id bigint NOT NULL,
    departure_date bytea,
    destination character varying(255),
    return_date bytea,
    source character varying(255)
);


ALTER TABLE flight_query OWNER TO postgres;

--
-- Name: flight_result; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE flight_result (
    id bigint NOT NULL,
    amount character varying(255),
    country integer,
    currency character varying(255),
    departure character varying(255),
    duration character varying(255),
    ip character varying(255),
    provider integer,
    query_time bytea,
    flight_query bigint NOT NULL
);


ALTER TABLE flight_result OWNER TO postgres;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO postgres;

--
-- Name: portals; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE portals (
    flight_query_id bigint NOT NULL,
    portal integer NOT NULL
);


ALTER TABLE portals OWNER TO postgres;

--
-- Name: providers; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE providers (
    flight_provider_id bigint NOT NULL,
    providers integer NOT NULL
);


ALTER TABLE providers OWNER TO postgres;

--
-- Data for Name: flight_query; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY flight_query (id, departure_date, destination, return_date, source) FROM stdin;
10	\\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770e05000007e0010a13162924b76a0078	Malpe	\\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770e05000007e0011113162924b76a0078	Amsterdam
\.


--
-- Data for Name: flight_result; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY flight_result (id, amount, country, currency, departure, duration, ip, provider, query_time, flight_query) FROM stdin;
\.


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 1, true);


--
-- Data for Name: portals; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY portals (flight_query_id, portal) FROM stdin;
10	0
10	1
10	2
10	3
10	4
10	5
10	6
10	7
10	8
10	9
10	10
10	11
10	12
10	13
10	14
10	15
10	16
10	17
10	18
10	19
10	20
10	21
10	22
10	23
10	24
10	25
10	26
\.


--
-- Data for Name: providers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY providers (flight_provider_id, providers) FROM stdin;
10	1
\.


--
-- Name: flight_query_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY flight_query
    ADD CONSTRAINT flight_query_pkey PRIMARY KEY (id);


--
-- Name: flight_result_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY flight_result
    ADD CONSTRAINT flight_result_pkey PRIMARY KEY (id);


--
-- Name: fk_5pvm482jflenmpfsx5etgvfce; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY providers
    ADD CONSTRAINT fk_5pvm482jflenmpfsx5etgvfce FOREIGN KEY (flight_provider_id) REFERENCES flight_query(id);


--
-- Name: fk_corjhdjd7ifdocjfwyarciybk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY portals
    ADD CONSTRAINT fk_corjhdjd7ifdocjfwyarciybk FOREIGN KEY (flight_query_id) REFERENCES flight_query(id);


--
-- Name: fk_om9dnyqvkk28l0f5yn36k2n0v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY flight_result
    ADD CONSTRAINT fk_om9dnyqvkk28l0f5yn36k2n0v FOREIGN KEY (flight_query) REFERENCES flight_query(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

