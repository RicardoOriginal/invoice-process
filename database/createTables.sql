-- DROP TABLE public.file;

CREATE TABLE if not Exists public.file (
	id bigserial NOT NULL,
	status text NOT NULL,
	creation_date_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	update_date_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT file_pkey PRIMARY KEY (id)
);
