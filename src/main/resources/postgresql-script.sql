CREATE TABLE public.caleg (
	id uuid NOT NULL,
	jenis_kelamin varchar(255) NOT NULL,
	nama varchar(255) NOT NULL,
	nomor_urut int4 NOT NULL,
	dapil_id uuid NOT NULL,
	partai_id uuid NOT NULL,
	CONSTRAINT caleg_jenis_kelamin_check CHECK (((jenis_kelamin)::text = ANY ((ARRAY['LAKILAKI'::character varying, 'PEREMPUAN'::character varying])::text[]))),
	CONSTRAINT caleg_pkey PRIMARY KEY (id)
);
ALTER TABLE public.caleg ADD CONSTRAINT fkjm3utxjucqou32awtddbgmyxq FOREIGN KEY (dapil_id) REFERENCES public.dapil(id);
ALTER TABLE public.caleg ADD CONSTRAINT fkoh8t8idintw81fo165q461taq FOREIGN KEY (partai_id) REFERENCES public.partai(id);


CREATE TABLE public.dapil (
	id uuid NOT NULL,
	jumlah_kursi int4 NOT NULL,
	nama_dapil varchar(255) NOT NULL,
	provinsi varchar(255) NOT NULL,
	CONSTRAINT dapil_pkey PRIMARY KEY (id),
	CONSTRAINT uk_h23vj9w2xyjj4t0qlbdo5w8x5 UNIQUE (nama_dapil)
);


CREATE TABLE public.partai (
	id uuid NOT NULL,
	nama_partai varchar(255) NOT NULL,
	nomor_urut int4 NOT NULL,
	CONSTRAINT partai_pkey PRIMARY KEY (id),
	CONSTRAINT uk_9ji4om8rhkiq7a9k0h7cfqad7 UNIQUE (nomor_urut),
	CONSTRAINT uk_f0rr2ah7k4ru004mn5raqh405 UNIQUE (nama_partai)
);


CREATE TABLE public.wilayah_dapil (
	id uuid NOT NULL,
	nama_wilayah varchar(255) NOT NULL,
	dapil_id uuid NOT NULL,
	CONSTRAINT wilayah_dapil_pkey PRIMARY KEY (id)
);
ALTER TABLE public.wilayah_dapil ADD CONSTRAINT fkgomq67ppedpp1p4troulla76a FOREIGN KEY (dapil_id) REFERENCES public.dapil(id);



INSERT INTO public.caleg (id,jenis_kelamin,nama,nomor_urut,dapil_id,partai_id) VALUES
	 ('4b1a4b44-2cc4-4d16-899e-b54ad10a8000','LAKILAKI','Budi',1,'f47ac10b-58cc-4372-a567-0e02b2c3d479','9f8c0425-10c7-4c88-bc28-e571920e0cba'),
	 ('c1d2e3f4-5678-49ab-90cd-ef12ab34cd56','PEREMPUAN','Siti',2,'a8f92c6d-1b34-4e78-905f-7cda98123b45','5b6c7d8e-9f01-4a23-b456-789c012d34ef'),
	 ('f8a9b7c6-d123-4e5f-89a0-bcdef0123456','PEREMPUAN','Nurul',2,'f47ac10b-58cc-4372-a567-0e02b2c3d479','5b6c7d8e-9f01-4a23-b456-789c012d34ef'),
	 ('1c2d3e4f-5678-49ab-90cd-ef12a345b678','LAKILAKI','Joko',1,'a8f92c6d-1b34-4e78-905f-7cda98123b45','9f8c0425-10c7-4c88-bc28-e571920e0cba');

	
INSERT INTO public.partai (id,nama_partai,nomor_urut) VALUES
	 ('9f8c0425-10c7-4c88-bc28-e571920e0cba','Gerindra',1),
	 ('5b6c7d8e-9f01-4a23-b456-789c012d34ef','Golkar',2);

	
INSERT INTO public.dapil (id,jumlah_kursi,nama_dapil,provinsi) VALUES
	 ('f47ac10b-58cc-4372-a567-0e02b2c3d479',10,'Dapil A','Jawa Barat'),
	 ('a8f92c6d-1b34-4e78-905f-7cda98123b45',15,'Dapil B','Jawa Timur');

	
INSERT INTO public.wilayah_dapil (id,nama_wilayah,dapil_id) VALUES
	 ('6d8f015f-12f5-4d2d-bb55-bfbd1c95db5b','Bandung','f47ac10b-58cc-4372-a567-0e02b2c3d479'),
	 ('3e1a2b4c-567d-48f0-9a6b-12c34d567e89','Surabaya','a8f92c6d-1b34-4e78-905f-7cda98123b45');
