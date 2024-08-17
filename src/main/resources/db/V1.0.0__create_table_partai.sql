CREATE TABLE IF NOT EXISTS public.partai
(
    id uuid NOT NULL,
    nama_partai character varying(255) COLLATE pg_catalog."default" NOT NULL,
    nomo_urut integer NOT NULL,
    CONSTRAINT partai_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;