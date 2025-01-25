--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3 (Debian 16.3-1.pgdg120+1)
-- Dumped by pg_dump version 16.3

-- Started on 2025-01-25 19:53:02

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3377 (class 1262 OID 32836)
-- Name: allobank_test; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE allobank_test WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE allobank_test OWNER TO postgres;

\connect allobank_test

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3378 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 32847)
-- Name: caleg; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.caleg (
    id character varying NOT NULL,
    dapil_id character varying,
    partai_id character varying,
    nomor_urut integer,
    nama character varying,
    jenis_kelamin character varying
);


ALTER TABLE public.caleg OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 32842)
-- Name: dapil; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dapil (
    id character varying NOT NULL,
    nama_dapil character varying,
    provinsi character varying,
    jumlah_kursi integer
);


ALTER TABLE public.dapil OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 32837)
-- Name: partai; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.partai (
    id character varying NOT NULL,
    nama_partai character varying,
    nomor_urut integer
);


ALTER TABLE public.partai OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 32866)
-- Name: wilayah_dapil; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wilayah_dapil (
    id character varying NOT NULL,
    dapil_id character varying,
    nama_wilayah character varying
);


ALTER TABLE public.wilayah_dapil OWNER TO postgres;

--
-- TOC entry 3370 (class 0 OID 32847)
-- Dependencies: 217
-- Data for Name: caleg; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.caleg VALUES ('16ceb980-281f-499d-9868-12de514aebc2', 'fba8d7ba-2c53-4025-aa70-f296c998ef5c', 'a4a83280-5608-49c0-a365-c64b00dbe868', 1, 'ACEP JAMALUDIN, S.Hum.', 'LAKILAKI');
INSERT INTO public.caleg VALUES ('8c161ff5-035b-4b98-9920-8f50bfd572e9', 'fba8d7ba-2c53-4025-aa70-f296c998ef5c', '793b3904-74ba-4c09-8970-df5b1c341020', 2, 'Dr. BUKY WIBAWA, M.Si.', 'LAKILAKI');
INSERT INTO public.caleg VALUES ('efebe78c-c892-479a-b693-d69c7f5f5c28', 'fba8d7ba-2c53-4025-aa70-f296c998ef5c', '5075c86c-149a-4de9-8d5a-caca5e3ffd78', 3, 'RAFAEL SITUMORANG, S.H.', 'LAKILAKI');
INSERT INTO public.caleg VALUES ('b64b7a9a-d66f-4e5a-9655-03ace39f2a99', '3a453f93-53e0-434f-bd17-436063edb3e3', 'a4a83280-5608-49c0-a365-c64b00dbe868', 1, 'MUHAMMAD NASIR, S.E.', 'LAKILAKI');
INSERT INTO public.caleg VALUES ('a258721c-9417-485c-b3fd-f844b64f4757', '3a453f93-53e0-434f-bd17-436063edb3e3', '793b3904-74ba-4c09-8970-df5b1c341020', 1, 'PUTRA ABSOR HASIBUAN, S.H.', 'LAKILAKI');
INSERT INTO public.caleg VALUES ('fd8253f1-a168-4cf7-9412-67c1cf9935ef', '3a453f93-53e0-434f-bd17-436063edb3e3', '5075c86c-149a-4de9-8d5a-caca5e3ffd78', 6, 'Ir. DAULAT SITORUS', 'LAKILAKI');


--
-- TOC entry 3369 (class 0 OID 32842)
-- Dependencies: 216
-- Data for Name: dapil; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.dapil VALUES ('fba8d7ba-2c53-4025-aa70-f296c998ef5c', 'Jawa Barat 1', 'Jawa Barat', 8);
INSERT INTO public.dapil VALUES ('3a453f93-53e0-434f-bd17-436063edb3e3', 'Jambi 1', 'Jambi', 10);


--
-- TOC entry 3368 (class 0 OID 32837)
-- Dependencies: 215
-- Data for Name: partai; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.partai VALUES ('a4a83280-5608-49c0-a365-c64b00dbe868', 'Partai Kebangkitan Bangsa', 1);
INSERT INTO public.partai VALUES ('793b3904-74ba-4c09-8970-df5b1c341020', 'Partai GERINDRA', 2);
INSERT INTO public.partai VALUES ('5075c86c-149a-4de9-8d5a-caca5e3ffd78', 'Partai Demokrasi Indonesia Perjuangan', 3);
INSERT INTO public.partai VALUES ('3c08e247-e090-4b48-ba2d-6b1a351d2fce', 'Partai Golongan Karya', 4);


--
-- TOC entry 3371 (class 0 OID 32866)
-- Dependencies: 218
-- Data for Name: wilayah_dapil; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.wilayah_dapil VALUES ('c67ccec2-55fa-43c6-848a-35cca1062331

', 'fba8d7ba-2c53-4025-aa70-f296c998ef5c', 'Bandung');
INSERT INTO public.wilayah_dapil VALUES ('8477a97d-081c-479a-b47a-a801ce0a8876

', 'fba8d7ba-2c53-4025-aa70-f296c998ef5c', 'Cimahi');
INSERT INTO public.wilayah_dapil VALUES ('0bd4a3d7-8ca3-4278-b405-8c45b569833c', '3a453f93-53e0-434f-bd17-436063edb3e3', 'Jambi');


--
-- TOC entry 3219 (class 2606 OID 41035)
-- Name: caleg caleg_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caleg
    ADD CONSTRAINT caleg_pk PRIMARY KEY (id);


--
-- TOC entry 3217 (class 2606 OID 41037)
-- Name: dapil dapil_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dapil
    ADD CONSTRAINT dapil_pk PRIMARY KEY (id);


--
-- TOC entry 3215 (class 2606 OID 41033)
-- Name: partai partai_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partai
    ADD CONSTRAINT partai_pk PRIMARY KEY (id);


--
-- TOC entry 3221 (class 2606 OID 41031)
-- Name: wilayah_dapil wilayah_dapil_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wilayah_dapil
    ADD CONSTRAINT wilayah_dapil_pk PRIMARY KEY (id);


--
-- TOC entry 3222 (class 2606 OID 41038)
-- Name: caleg caleg_dapil_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caleg
    ADD CONSTRAINT caleg_dapil_fk FOREIGN KEY (dapil_id) REFERENCES public.dapil(id);


--
-- TOC entry 3223 (class 2606 OID 41043)
-- Name: caleg caleg_partai_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caleg
    ADD CONSTRAINT caleg_partai_fk FOREIGN KEY (partai_id) REFERENCES public.partai(id);


--
-- TOC entry 3224 (class 2606 OID 41048)
-- Name: wilayah_dapil wilayah_dapil_dapil_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wilayah_dapil
    ADD CONSTRAINT wilayah_dapil_dapil_fk FOREIGN KEY (dapil_id) REFERENCES public.dapil(id);


-- Completed on 2025-01-25 19:53:03

--
-- PostgreSQL database dump complete
--

