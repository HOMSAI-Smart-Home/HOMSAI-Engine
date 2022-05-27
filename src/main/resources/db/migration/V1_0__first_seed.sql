
CREATE TABLE IF NOT EXISTS  public.media (
    uuid character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    deleted_at timestamp without time zone,
    origin_reference character varying(255),
    updated_at timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    custom_tag character varying(255),
    file_name character varying(255) NOT NULL,
    mimetype character varying(255) NOT NULL,
    original_extension character varying(255) NOT NULL,
    original_file_name character varying(255),
    media_size bigint NOT NULL,
    media_tag character varying(255) NOT NULL,
    media_type integer,
    creator_uuid character varying(255)
);


CREATE TABLE IF NOT EXISTS  public.media_entity (
    uuid character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    deleted_at timestamp without time zone,
    origin_reference character varying(255),
    updated_at timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    entity_uuid character varying(255),
    media_uuid character varying(255) NOT NULL,
    creator_uuid character varying(255)
);


ALTER TABLE  public.media_entity
    ADD CONSTRAINT media_entity_pkey PRIMARY KEY (uuid);

ALTER TABLE  public.media
    ADD CONSTRAINT media_pkey PRIMARY KEY (uuid);


ALTER TABLE  public.media_entity
    ADD CONSTRAINT fks6vbmfaulwmgx3nl8vniixlqo FOREIGN KEY (media_uuid) REFERENCES public.media(uuid);


