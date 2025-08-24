--
-- PostgreSQL database dump
--

\restrict ioP1hLiazwBHhgQTG4viZxtfKwWN9ILa1HdpEEJj3eIIziwhxKOkvhwGV3pTEH1

-- Dumped from database version 15.14 (Debian 15.14-1.pgdg13+1)
-- Dumped by pg_dump version 15.14 (Debian 15.14-1.pgdg13+1)

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: caleg; Type: TABLE; Schema: public; Owner: pemilu
--

CREATE TABLE public.caleg (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    jenis_kelamin character varying(255) NOT NULL,
    nama character varying(100) NOT NULL,
    nomor_urut integer NOT NULL,
    updated_at timestamp(6) without time zone,
    dapil_id uuid NOT NULL,
    partai_id uuid NOT NULL,
    CONSTRAINT caleg_jenis_kelamin_check CHECK (((jenis_kelamin)::text = ANY ((ARRAY['LAKILAKI'::character varying, 'PEREMPUAN'::character varying])::text[])))
);


ALTER TABLE public.caleg OWNER TO pemilu;

--
-- Name: dapil; Type: TABLE; Schema: public; Owner: pemilu
--

CREATE TABLE public.dapil (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    jumlah_kursi integer NOT NULL,
    nama_dapil character varying(255) NOT NULL,
    provinsi character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone
);


ALTER TABLE public.dapil OWNER TO pemilu;

--
-- Name: dapil_wilayah; Type: TABLE; Schema: public; Owner: pemilu
--

CREATE TABLE public.dapil_wilayah (
    dapil_id uuid NOT NULL,
    wilayah character varying(255)
);


ALTER TABLE public.dapil_wilayah OWNER TO pemilu;

--
-- Name: partai; Type: TABLE; Schema: public; Owner: pemilu
--

CREATE TABLE public.partai (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    nama_partai character varying(100) NOT NULL,
    nomor_urut integer NOT NULL,
    updated_at timestamp(6) without time zone
);


ALTER TABLE public.partai OWNER TO pemilu;

--
-- Data for Name: caleg; Type: TABLE DATA; Schema: public; Owner: pemilu
--

COPY public.caleg (id, created_at, jenis_kelamin, nama, nomor_urut, updated_at, dapil_id, partai_id) FROM stdin;
\.


--
-- Data for Name: dapil; Type: TABLE DATA; Schema: public; Owner: pemilu
--

COPY public.dapil (id, created_at, jumlah_kursi, nama_dapil, provinsi, updated_at) FROM stdin;
bf8c881e-5dcb-4e99-83ea-6ee8fb574d43	2025-08-23 15:40:06.486007	1	Dapil Jawa Barat II	Jawa Barat	2025-08-23 15:40:06.486055
5c6e4e95-b3b0-4482-81ef-56e92e39d4a0	2025-08-23 19:52:56.903759	1	Dapil Jawa Tengah 2	Jawa Barat	2025-08-24 08:05:21.987741
\.


--
-- Data for Name: dapil_wilayah; Type: TABLE DATA; Schema: public; Owner: pemilu
--

COPY public.dapil_wilayah (dapil_id, wilayah) FROM stdin;
bf8c881e-5dcb-4e99-83ea-6ee8fb574d43	Bandung Barat
5c6e4e95-b3b0-4482-81ef-56e92e39d4a0	Bandung Barat
\.


--
-- Data for Name: partai; Type: TABLE DATA; Schema: public; Owner: pemilu
--

COPY public.partai (id, created_at, nama_partai, nomor_urut, updated_at) FROM stdin;
dd4bde8a-40c7-45b2-aa95-2dc77ce8499d	2025-08-23 19:05:06.105535	terang1	1	2025-08-23 19:05:06.105639
\.


--
-- Name: caleg caleg_pkey; Type: CONSTRAINT; Schema: public; Owner: pemilu
--

ALTER TABLE ONLY public.caleg
    ADD CONSTRAINT caleg_pkey PRIMARY KEY (id);


--
-- Name: dapil dapil_pkey; Type: CONSTRAINT; Schema: public; Owner: pemilu
--

ALTER TABLE ONLY public.dapil
    ADD CONSTRAINT dapil_pkey PRIMARY KEY (id);


--
-- Name: partai partai_pkey; Type: CONSTRAINT; Schema: public; Owner: pemilu
--

ALTER TABLE ONLY public.partai
    ADD CONSTRAINT partai_pkey PRIMARY KEY (id);


--
-- Name: partai uk_9ji4om8rhkiq7a9k0h7cfqad7; Type: CONSTRAINT; Schema: public; Owner: pemilu
--

ALTER TABLE ONLY public.partai
    ADD CONSTRAINT uk_9ji4om8rhkiq7a9k0h7cfqad7 UNIQUE (nomor_urut);


--
-- Name: partai uk_f0rr2ah7k4ru004mn5raqh405; Type: CONSTRAINT; Schema: public; Owner: pemilu
--

ALTER TABLE ONLY public.partai
    ADD CONSTRAINT uk_f0rr2ah7k4ru004mn5raqh405 UNIQUE (nama_partai);


--
-- Name: dapil uk_h23vj9w2xyjj4t0qlbdo5w8x5; Type: CONSTRAINT; Schema: public; Owner: pemilu
--

ALTER TABLE ONLY public.dapil
    ADD CONSTRAINT uk_h23vj9w2xyjj4t0qlbdo5w8x5 UNIQUE (nama_dapil);


--
-- Name: dapil_wilayah fkb72ac60eykm2ll43fgwl3wabm; Type: FK CONSTRAINT; Schema: public; Owner: pemilu
--

ALTER TABLE ONLY public.dapil_wilayah
    ADD CONSTRAINT fkb72ac60eykm2ll43fgwl3wabm FOREIGN KEY (dapil_id) REFERENCES public.dapil(id);


--
-- Name: caleg fkjm3utxjucqou32awtddbgmyxq; Type: FK CONSTRAINT; Schema: public; Owner: pemilu
--

ALTER TABLE ONLY public.caleg
    ADD CONSTRAINT fkjm3utxjucqou32awtddbgmyxq FOREIGN KEY (dapil_id) REFERENCES public.dapil(id) ON DELETE CASCADE;


--
-- Name: caleg fkoh8t8idintw81fo165q461taq; Type: FK CONSTRAINT; Schema: public; Owner: pemilu
--

ALTER TABLE ONLY public.caleg
    ADD CONSTRAINT fkoh8t8idintw81fo165q461taq FOREIGN KEY (partai_id) REFERENCES public.partai(id);


--
-- PostgreSQL database dump complete
--

\unrestrict ioP1hLiazwBHhgQTG4viZxtfKwWN9ILa1HdpEEJj3eIIziwhxKOkvhwGV3pTEH1

