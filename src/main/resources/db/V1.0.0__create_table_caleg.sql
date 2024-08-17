CREATE TABLE IF NOT EXISTS public.caleg
(
    id uuid NOT NULL,
    jenis_kelamin smallint NOT NULL,
    nama character varying(255) COLLATE pg_catalog."default" NOT NULL,
    nomor_urut integer NOT NULL,
    dapil_id uuid NOT NULL,
    partai_id uuid NOT NULL,
    CONSTRAINT caleg_pkey PRIMARY KEY (id),
    CONSTRAINT fkjm3utxjucqou32awtddbgmyxq FOREIGN KEY (dapil_id)
        REFERENCES public.dapil (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkoh8t8idintw81fo165q461taq FOREIGN KEY (partai_id)
        REFERENCES public.partai (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT caleg_jenis_kelamin_check CHECK (jenis_kelamin >= 0 AND jenis_kelamin <= 1)
)

TABLESPACE pg_default;