--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

-- Started on 2025-08-24 20:50:49

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
-- TOC entry 215 (class 1259 OID 69491)
-- Name: electoral_district; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.electoral_district (
    id uuid NOT NULL,
    seat_count integer NOT NULL,
    name character varying(50) NOT NULL,
    province character varying(50) NOT NULL,
    region character varying(255)[] NOT NULL
);


ALTER TABLE public.electoral_district OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 69498)
-- Name: legislative; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.legislative (
    id uuid NOT NULL,
    gender character varying(255) NOT NULL,
    name character varying(50) NOT NULL,
    order_number integer,
    electoral_district_id uuid NOT NULL,
    party_id uuid NOT NULL,
    CONSTRAINT legislative_gender_check CHECK (((gender)::text = ANY ((ARRAY['LAKILAKI'::character varying, 'PEREMPUAN'::character varying])::text[])))
);


ALTER TABLE public.legislative OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 69504)
-- Name: party; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.party (
    id uuid NOT NULL,
    name character varying(50) NOT NULL,
    order_number integer NOT NULL
);


ALTER TABLE public.party OWNER TO postgres;

--
-- TOC entry 4847 (class 0 OID 69491)
-- Dependencies: 215
-- Data for Name: electoral_district; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.electoral_district (id, seat_count, name, province, region) FROM stdin;
3873513d-f3f4-4594-b800-d00039d00a33	7	ACEH I	Aceh	{"Aceh Selatan","Aceh Tenggara","Aceh Barat","Aceh Besar",Pidie,Simeulue,"Aceh Singkil","Aceh Barat Daya","Gayo Lues","Aceh Jaya","Nagan Raya","Pidie Jaya","Kota Banda Aceh","Kota Sabang","Kota Subulussalam"}
a43aaf27-b908-4cdd-8452-561db8abfcd5	6	ACEH II	Aceh	{"Aceh Timur","Aceh Tengah","Aceh Utara",Bireuen,"Aceh Tamiang","Bener Meriah","Kota Lhokseumawe","Kota Langsa"}
c365ada3-eb82-4b84-b7f9-d0e3e6861bde	10	SUMATERA UTARA I	Sumatera Utara	{"Deli Serdang","Serdang Bedagai","Kota Medan","Kota Tebing Tinggi"}
e10b3f7d-3733-4ee7-94df-db637cdc56ba	10	SUMATERA UTARA II	Sumatera Utara	{"Tapanuli Tengah","Tapanuli Utara","Tapanuli Selatan",Nias,Labuhanbatu,Toba,"Mandailing Natal","Nias Selatan","Humbang Hasundutan",Samosir,"Padang Lawas Utara","Padang Lawas","Labuhanbatu Selatan","Labuhanbatu Utara","Nias Utara","Nias Barat","Kota Sibolga","Kota Padang Sidempuan","Kota Gunungsitoli"}
28b8e6c8-6d65-4ebf-a6d8-68c977ccf80f	10	SUMATERA UTARA III	Sumatera Utara	{Langkat,Karo,Simalungun,Asahan,Dairi,"Pakpak Bharat","Batu Bara","Kota Pematang Siantar","Kota Tanjung Balai","Kota Binjai"}
c683fb44-4b00-462b-8873-ba3c12d04cdd	8	SUMATERA BARAT I	Sumatera Barat	{"Pesisir Selatan",Solok,Sijunjung,"Tanah Datar","Kepulauan Mentawai",Dharmasraya,"Solok Selatan","Kota Padang","Kota Solok","Kota Sawahlunto","Kota Padang Panjang"}
af85661e-d275-4ec9-bd11-a8994ad530db	6	SUMATERA BARAT II	Sumatera Barat	{"Padang Pariaman",Agam,"Lima Puluh Kota",Pasaman,"Pasaman Barat","Kota Bukittinggi","Kota Payakumbuh","Kota Pariaman"}
\.


--
-- TOC entry 4848 (class 0 OID 69498)
-- Dependencies: 216
-- Data for Name: legislative; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.legislative (id, gender, name, order_number, electoral_district_id, party_id) FROM stdin;
5c83543f-7bd9-4040-ad26-419411299fb9	LAKILAKI	Faisal Abdullah	1	3873513d-f3f4-4594-b800-d00039d00a33	163bdaa6-51a5-4e6a-afa8-51d47138f61f
06669065-3763-4a9e-b051-ce7482cb604b	PEREMPUAN	Nur Aisyah	2	3873513d-f3f4-4594-b800-d00039d00a33	55784290-91f3-4702-b51e-e333c35a9721
415995d5-d2a6-41bd-9046-8b127a6b19cc	LAKILAKI	Hendra Saputra	3	a43aaf27-b908-4cdd-8452-561db8abfcd5	81afa26b-4d4e-4876-bae5-f08e56f63e5b
e05a8212-09dd-4b60-8d46-62eae3de4b3d	PEREMPUAN	Maya Sari	4	a43aaf27-b908-4cdd-8452-561db8abfcd5	fbc85af6-182e-488c-9390-b7c6532fddee
074eb61d-9527-4eaf-8a70-bb34acde1676	LAKILAKI	Ridwan Harahap	1	c365ada3-eb82-4b84-b7f9-d0e3e6861bde	578dec61-9fb9-462c-bb48-561859287766
3ebb3844-99dc-4419-a092-563a356b04e4	PEREMPUAN	Siti Marlina	2	c365ada3-eb82-4b84-b7f9-d0e3e6861bde	a8ec3d25-5701-4f4e-85a9-7d85b90ec8f6
b78f7c72-692c-4c03-b324-01b76613df72	LAKILAKI	Ahmad Nasution	1	e10b3f7d-3733-4ee7-94df-db637cdc56ba	1d2096c4-6267-42d5-a1b6-f8acf29f2855
3e00196a-cbc8-4047-ab29-3c4ae8b4b2be	PEREMPUAN	Dewi Anggraini	2	e10b3f7d-3733-4ee7-94df-db637cdc56ba	a5a8adfd-f559-4675-a431-823b9961db2d
795998c2-68b3-41b8-b229-75502742d36e	LAKILAKI	Syamsul Bahri	1	28b8e6c8-6d65-4ebf-a6d8-68c977ccf80f	f0de7c03-55fb-4865-acd2-e2429acfccd0
9da1772f-b287-416c-9500-fe17ecc4ee7d	PEREMPUAN	Rosmawati	2	28b8e6c8-6d65-4ebf-a6d8-68c977ccf80f	f9f406ff-60bc-43e7-80b7-83f511d169d5
0583c7fc-82b5-4439-814e-7878817a93cd	LAKILAKI	Fauzan Malik	1	c683fb44-4b00-462b-8873-ba3c12d04cdd	47855bff-793f-44c7-9df4-159ded8c0320
3cf5c33b-6de0-4829-8bda-8e31a4f752b2	PEREMPUAN	Anita Rahma	2	c683fb44-4b00-462b-8873-ba3c12d04cdd	37189ad3-3c5f-45fe-b60f-3bb9bfbcd117
44fb19ed-b9c4-4895-ad25-04352f10c3e6	PEREMPUAN	Putri Melati	2	af85661e-d275-4ec9-bd11-a8994ad530db	d0418153-515d-472a-9975-3ce4b5bf56d2
\.


--
-- TOC entry 4849 (class 0 OID 69504)
-- Dependencies: 217
-- Data for Name: party; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.party (id, name, order_number) FROM stdin;
3d26c27a-0161-4422-973f-d26d55482eee	Partai Buruh	6
578dec61-9fb9-462c-bb48-561859287766	Partai Kebangkitan Bangsa	1
55784290-91f3-4702-b51e-e333c35a9721	Partai Gerakan Indonesia Raya	2
163bdaa6-51a5-4e6a-afa8-51d47138f61f	Partai Demokrasi Indonesia Perjuangan	3
47855bff-793f-44c7-9df4-159ded8c0320	Partai Gelombang Rakyat Indonesia	7
f0de7c03-55fb-4865-acd2-e2429acfccd0	Partai Keadilan Sejahtera	8
9e758dab-3ef6-40ad-ae27-16988430e5e9	Partai Kebangkitan Nusantara	9
f9f406ff-60bc-43e7-80b7-83f511d169d5	Partai Hati Nurani Rakyat	10
acf41219-8707-41fe-8438-2831c2fa15ea	Partai Garda Perubahan Indonesia	11
a8ec3d25-5701-4f4e-85a9-7d85b90ec8f6	Partai Amanat Nasional	12
37189ad3-3c5f-45fe-b60f-3bb9bfbcd117	Partai Bulan Bintang	13
a5a8adfd-f559-4675-a431-823b9961db2d	Partai Demokrat	14
d0418153-515d-472a-9975-3ce4b5bf56d2	Partai Solidaritas Indonesia	15
01572384-0ab0-44da-9778-1d1e883dd50f	Partai Perindo	16
1d2096c4-6267-42d5-a1b6-f8acf29f2855	Partai Persatuan Pembangunan	17
81afa26b-4d4e-4876-bae5-f08e56f63e5b	Partai Golongan Karya	4
fbc85af6-182e-488c-9390-b7c6532fddee	Partai Nasional Demokrat	5
\.


--
-- TOC entry 4697 (class 2606 OID 69497)
-- Name: electoral_district electoral_district_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.electoral_district
    ADD CONSTRAINT electoral_district_pkey PRIMARY KEY (id);


--
-- TOC entry 4699 (class 2606 OID 69503)
-- Name: legislative legislative_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.legislative
    ADD CONSTRAINT legislative_pkey PRIMARY KEY (id);


--
-- TOC entry 4701 (class 2606 OID 69508)
-- Name: party party_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.party
    ADD CONSTRAINT party_pkey PRIMARY KEY (id);


--
-- TOC entry 4702 (class 2606 OID 69509)
-- Name: legislative fk1tkhqbuahf2wgc94mxlkkbusk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.legislative
    ADD CONSTRAINT fk1tkhqbuahf2wgc94mxlkkbusk FOREIGN KEY (electoral_district_id) REFERENCES public.electoral_district(id);


--
-- TOC entry 4703 (class 2606 OID 69514)
-- Name: legislative fkm7cgv3psosglxv7ic2l53sxir; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.legislative
    ADD CONSTRAINT fkm7cgv3psosglxv7ic2l53sxir FOREIGN KEY (party_id) REFERENCES public.party(id);


-- Completed on 2025-08-24 20:50:49

--
-- PostgreSQL database dump complete
--

