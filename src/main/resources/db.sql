-- 3 +/- SELECT COUNT(*) FROM public.caleg;
-- 3 +/- SELECT COUNT(*) FROM public.dapil;
-- 3 +/- SELECT COUNT(*) FROM public.partai;
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."CONSTRAINT_5" UNIQUE("dapil_id");
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."CONSTRAINT_5A" UNIQUE("partai_id");
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."CONSTRAINT_5A0" CHECK("jenis_kelamin" IN('LAKILAKI', 'PEREMPUAN')) NOCHECK;
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."CONSTRAINT_5A0D" PRIMARY KEY("id");
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."fkjm3utxjucqou32awtddbgmyxq" FOREIGN KEY("dapil_id") REFERENCES "public"."dapil"("id") NOCHECK;
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."fkoh8t8idintw81fo165q461taq" FOREIGN KEY("partai_id") REFERENCES "public"."partai"("id") NOCHECK;
ALTER TABLE "public"."dapil" ADD CONSTRAINT "public"."CONSTRAINT_5AE" PRIMARY KEY("id");
ALTER TABLE "public"."partai" ADD CONSTRAINT "public"."CONSTRAINT_C" PRIMARY KEY("id");
CREATE CACHED TABLE "public"."caleg"(
    "nomor_urut" INTEGER,
    "dapil_id" UUID,
    "id" UUID NOT NULL,
    "partai_id" UUID,
    "jenis_kelamin" CHARACTER VARYING(255),
    "nama" CHARACTER VARYING(255)
);
CREATE CACHED TABLE "public"."dapil"(
    "jumlah_kursi" INTEGER,
    "id" UUID NOT NULL,
    "nama_dapil" CHARACTER VARYING(255),
    "provinsi" CHARACTER VARYING(255),
    "wilayah_dapil_list" CHARACTER VARYING(255) ARRAY
);
CREATE CACHED TABLE "public"."partai"(
    "nomor_urut" INTEGER,
    "id" UUID NOT NULL,
    "nama_partai" CHARACTER VARYING(255)
);
CREATE USER IF NOT EXISTS "SA" SALT 'fe1a4a89a0629cfa' HASH '68589a715eecb6cb32e3b7714c55f699f44eb6190e63e0ae50d373cc969a8cb3' ADMIN;
CREATE CACHED TABLE "public"."caleg"(
    "nomor_urut" INTEGER,
    "dapil_id" UUID,
    "id" UUID NOT NULL,
    "partai_id" UUID,
    "jenis_kelamin" CHARACTER VARYING(255),
    "nama" CHARACTER VARYING(255)
);
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."CONSTRAINT_5A0D" PRIMARY KEY("id");
-- 3 +/- SELECT COUNT(*) FROM public.caleg;
CREATE CACHED TABLE "public"."dapil"(
    "jumlah_kursi" INTEGER,
    "id" UUID NOT NULL,
    "nama_dapil" CHARACTER VARYING(255),
    "provinsi" CHARACTER VARYING(255),
    "wilayah_dapil_list" CHARACTER VARYING(255) ARRAY
);
ALTER TABLE "public"."dapil" ADD CONSTRAINT "public"."CONSTRAINT_5AE" PRIMARY KEY("id");
-- 3 +/- SELECT COUNT(*) FROM public.dapil;
CREATE CACHED TABLE "public"."partai"(
    "nomor_urut" INTEGER,
    "id" UUID NOT NULL,
    "nama_partai" CHARACTER VARYING(255)
);
ALTER TABLE "public"."partai" ADD CONSTRAINT "public"."CONSTRAINT_C" PRIMARY KEY("id");
-- 3 +/- SELECT COUNT(*) FROM public.partai;
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."CONSTRAINT_5A0" CHECK("jenis_kelamin" IN('LAKILAKI', 'PEREMPUAN')) NOCHECK;
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."CONSTRAINT_5A" UNIQUE("partai_id");
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."CONSTRAINT_5" UNIQUE("dapil_id");
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."fkjm3utxjucqou32awtddbgmyxq" FOREIGN KEY("dapil_id") REFERENCES "public"."dapil"("id") NOCHECK;
ALTER TABLE "public"."caleg" ADD CONSTRAINT "public"."fkoh8t8idintw81fo165q461taq" FOREIGN KEY("partai_id") REFERENCES "public"."partai"("id") NOCHECK;
