CREATE CACHED TABLE "PUBLIC"."MEDIA"(
    "UUID" UUID NOT NULL,
    "CREATED_AT" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    "DELETED_AT" TIMESTAMP WITHOUT TIME ZONE,
    "ORIGIN_REFERENCE" CHARACTER VARYING(255),
    "UPDATED_AT" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    "VERSION" BIGINT NOT NULL,
    "CUSTOM_TAG" CHARACTER VARYING(255),
    "FILE_NAME" CHARACTER VARYING(255) NOT NULL,
    "MIMETYPE" CHARACTER VARYING(255) NOT NULL,
    "ORIGINAL_EXTENSION" CHARACTER VARYING(255) NOT NULL,
    "ORIGINAL_FILE_NAME" CHARACTER VARYING(255),
    "MEDIA_SIZE" BIGINT NOT NULL,
    "MEDIA_TAG" CHARACTER VARYING(255) NOT NULL,
    "MEDIA_TYPE" INTEGER,
    "CREATOR_UUID" CHARACTER VARYING(255)
);
ALTER TABLE "PUBLIC"."MEDIA" ADD CONSTRAINT "PUBLIC"."MEDIA_PKEY" PRIMARY KEY("UUID");

CREATE CACHED TABLE "PUBLIC"."MEDIA_ENTITY"(
    "UUID" UUID NOT NULL,
    "CREATED_AT" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    "DELETED_AT" TIMESTAMP WITHOUT TIME ZONE,
    "ORIGIN_REFERENCE" CHARACTER VARYING(255),
    "UPDATED_AT" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    "VERSION" BIGINT NOT NULL,
    "ENTITY_UUID" CHARACTER VARYING(255),
    "MEDIA_UUID" CHARACTER VARYING(255) NOT NULL,
    "CREATOR_UUID" CHARACTER VARYING(255)
);
ALTER TABLE "PUBLIC"."MEDIA_ENTITY" ADD CONSTRAINT "PUBLIC"."MEDIA_ENTITY_PKEY" PRIMARY KEY("UUID");

CREATE CACHED TABLE "PUBLIC"."HA_AREAS"(
    "UUID" UUID NOT NULL,
    "CREATED_AT" TIMESTAMP NOT NULL,
    "DELETED_AT" TIMESTAMP,
    "ORIGIN_REFERENCE" VARCHAR(255),
    "UPDATED_AT" TIMESTAMP NOT NULL,
    "VERSION" BIGINT NOT NULL,
    "NAME" VARCHAR(255) NOT NULL,
    "DESIRED_WINTER_TEMPERATURE" DOUBLE,
    "DESIRED_SUMMER_TEMPERATURE" DOUBLE
);
ALTER TABLE "PUBLIC"."HA_AREAS" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_3" PRIMARY KEY("UUID");

CREATE CACHED TABLE "PUBLIC"."HA_ENTITIES_HOMSAI_ENTITIES"(
    "HOMSAI_ENTIITY_UUID" UUID NOT NULL,
    "HA_ENTITY_UUID" UUID NOT NULL
);

CREATE CACHED TABLE "PUBLIC"."HA_EXCLUDED_ENTITIES"(
    "UUID" UUID NOT NULL,
    "CREATED_AT" TIMESTAMP NOT NULL,
    "DELETED_AT" TIMESTAMP,
    "ORIGIN_REFERENCE" VARCHAR(255),
    "UPDATED_AT" TIMESTAMP NOT NULL,
    "VERSION" BIGINT NOT NULL,
    "ENTITY_ID" VARCHAR(255) NOT NULL
);
ALTER TABLE "PUBLIC"."HA_EXCLUDED_ENTITIES" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_2" PRIMARY KEY("UUID");

CREATE CACHED TABLE "PUBLIC"."HA_ENTITIES"(
    "UUID" UUID NOT NULL,
    "CREATED_AT" TIMESTAMP NOT NULL,
    "DELETED_AT" TIMESTAMP,
    "ORIGIN_REFERENCE" VARCHAR(255),
    "UPDATED_AT" TIMESTAMP NOT NULL,
    "VERSION" BIGINT NOT NULL,
    "DOMAIN" VARCHAR(255),
    "ENTITY_ID" VARCHAR(255) NOT NULL,
    "NAME" VARCHAR(255),
    "UNIT_OF_MEASUREMENT" VARCHAR(255),
    "DEVICE_CLASS" VARCHAR(255)
);
ALTER TABLE "PUBLIC"."HA_ENTITIES" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_B" PRIMARY KEY("UUID");

CREATE CACHED TABLE "PUBLIC"."ENTITIES_AREAS"(
    "ENTITY_UUID" UUID NOT NULL,
    "AREA_UUID" UUID NOT NULL
);

CREATE CACHED TABLE "PUBLIC"."HOMSAI_ENTITIES"(
    "UUID" UUID NOT NULL,
    "CREATED_AT" TIMESTAMP NOT NULL,
    "DELETED_AT" TIMESTAMP,
    "ORIGIN_REFERENCE" VARCHAR(255),
    "UPDATED_AT" TIMESTAMP NOT NULL,
    "VERSION" BIGINT NOT NULL,
    "NAME" VARCHAR(255) NOT NULL,
    "UNIT_OF_MEASUREMENT" VARCHAR(255),
    "AREA_UUID" UUID,
    "TYPE_UUID" UUID
);
ALTER TABLE "PUBLIC"."HOMSAI_ENTITIES" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_6" PRIMARY KEY("UUID");

CREATE CACHED TABLE "PUBLIC"."HOMSAI_ENTITY_TYPES"(
    "UUID" UUID NOT NULL,
    "CREATED_AT" TIMESTAMP NOT NULL,
    "DELETED_AT" TIMESTAMP,
    "ORIGIN_REFERENCE" VARCHAR(255),
    "UPDATED_AT" TIMESTAMP NOT NULL,
    "VERSION" BIGINT NOT NULL,
    "NAME" VARCHAR(255) NOT NULL,
    "UNIT_OF_MEASUREMENT" VARCHAR(255),
    "DEVICE_CLASS" VARCHAR(255),
    "ROOT_NAME" VARCHAR(255) NOT NULL,
    "OPERATOR" VARCHAR(255) NOT NULL
);
ALTER TABLE "PUBLIC"."HOMSAI_ENTITY_TYPES" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_A" PRIMARY KEY("UUID");

CREATE CACHED TABLE "PUBLIC"."HOMSAI_ENTITIES_HISTORICAL_STATES"(
    "UUID" UUID NOT NULL,
    "CREATED_AT" TIMESTAMP NOT NULL,
    "DELETED_AT" TIMESTAMP,
    "ORIGIN_REFERENCE" VARCHAR(255),
    "UPDATED_AT" TIMESTAMP NOT NULL,
    "VERSION" BIGINT NOT NULL,
    "TIMESTAMP" TIMESTAMP,
    "UNIT_OF_MEASUREMENT" VARCHAR(255),
    "VALUE" DOUBLE,
    "AREA_UUID" UUID,
    "TYPE_UUID" UUID
);
ALTER TABLE "PUBLIC"."HOMSAI_ENTITIES_HISTORICAL_STATES" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_2C" PRIMARY KEY("UUID");
CREATE INDEX "PUBLIC"."IDX_TIMESTAMP_DESC" ON "PUBLIC"."HOMSAI_ENTITIES_HISTORICAL_STATES"("TIMESTAMP" DESC);
CREATE INDEX "PUBLIC"."IDX_AREA" ON "PUBLIC"."HOMSAI_ENTITIES_HISTORICAL_STATES"("AREA_UUID");
CREATE INDEX "PUBLIC"."IDX_TYPE" ON "PUBLIC"."HOMSAI_ENTITIES_HISTORICAL_STATES"("TYPE_UUID");
CREATE INDEX "PUBLIC"."IDX_DELETED" ON "PUBLIC"."HOMSAI_ENTITIES_HISTORICAL_STATES"("DELETED_AT");

CREATE CACHED TABLE "PUBLIC"."HVAC_DEVICES"(
    "UUID" UUID NOT NULL,
    "CREATED_AT" TIMESTAMP NOT NULL,
    "DELETED_AT" TIMESTAMP,
    "ORIGIN_REFERENCE" VARCHAR(255),
    "UPDATED_AT" TIMESTAMP NOT NULL,
    "VERSION" BIGINT NOT NULL,
    "MAX_TEMP" DOUBLE,
    "MIN_TEMP" DOUBLE,
    "POWER_CONSUMPTION" DOUBLE,
    "TYPE" INTEGER,
    "AREA_UUID" UUID,
    "ENTITY_ID" VARCHAR(255),
    "ENABLED" BOOLEAN
);
ALTER TABLE "PUBLIC"."HVAC_DEVICES" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_1" PRIMARY KEY("UUID");

CREATE CACHED TABLE "PUBLIC"."HOME_INFO"(
    "UUID" UUID NOT NULL,
    "CREATED_AT" TIMESTAMP NOT NULL,
    "DELETED_AT" TIMESTAMP,
    "ORIGIN_REFERENCE" VARCHAR(255),
    "UPDATED_AT" TIMESTAMP NOT NULL,
    "VERSION" BIGINT NOT NULL,
    "GENERAL_POWER_METER_ID" VARCHAR(255),
    "HAS_PHOTOVOLTAIC" BOOLEAN,
    "HVAC_POWER_METER_ID" VARCHAR(255),
    "LATITUDE" DOUBLE,
    "LONGITUDE" DOUBLE,
    "PHOTOVOLTAIC_INSTALL_DATE" TIMESTAMP,
    "PHOTOVOLTAIC_PRODUCTION_SENSOR_ID" VARCHAR(255),
    "PHOTOVOLTAIC_STORAGE_SENSOR_ID" VARCHAR(255),
    "PHOTOVOLTAIC_PEAK_POWER" DOUBLE,
    "PHOTOVOLTAIC_OPTIMIZATIONS_ENABLED" BOOLEAN,
    "AISERVICE_TOKEN" VARCHAR(2048),
    "AISERVICE_REFRESH_TOKEN" VARCHAR(2048),
    "AISERVICE_EMAIL" VARCHAR(512)
);
ALTER TABLE "PUBLIC"."HOME_INFO" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_26" PRIMARY KEY("UUID");

CREATE CACHED TABLE "PUBLIC"."HVAC_MODES"(
    "HVAC_DEVICE_UUID" UUID NOT NULL,
    "HVAC_MODES" VARCHAR(255)
);

CREATE CACHED TABLE "PUBLIC"."HVAC_DEVICE_INTERVALS"(
    "HVAC_DEVICE_UUID" UUID NOT NULL,
    "START_TIME" TIME,
    "END_TIME" TIME
);

ALTER TABLE "PUBLIC"."ENTITIES_AREAS" ADD CONSTRAINT "PUBLIC"."FKJY7PS9PF1PS80UMDFFYAF7GC4" FOREIGN KEY("ENTITY_UUID") REFERENCES "PUBLIC"."HA_ENTITIES"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."HA_ENTITIES_HOMSAI_ENTITIES" ADD CONSTRAINT "PUBLIC"."FK78CY5D5NMHGRJIUELPETJBS0Q" FOREIGN KEY("HA_ENTITY_UUID") REFERENCES "PUBLIC"."HA_ENTITIES"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."ENTITIES_AREAS" ADD CONSTRAINT "PUBLIC"."FKAF7T3QXW8616QHRY1DCW7XAVY" FOREIGN KEY("AREA_UUID") REFERENCES "PUBLIC"."HA_AREAS"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."HVAC_MODES" ADD CONSTRAINT "PUBLIC"."FKLKHNQ1MCP1YFKHXV58YM4H1ML" FOREIGN KEY("HVAC_DEVICE_UUID") REFERENCES "PUBLIC"."HVAC_DEVICES"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."MEDIA_ENTITY" ADD CONSTRAINT "PUBLIC"."FKS6VBMFAULWMGX3NL8VNIIXLQO" FOREIGN KEY("MEDIA_UUID") REFERENCES "PUBLIC"."MEDIA"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."HOMSAI_ENTITIES_HISTORICAL_STATES" ADD CONSTRAINT "PUBLIC"."FKEGHY4T7SXEAFK9OJ52OT36NXR" FOREIGN KEY("TYPE_UUID") REFERENCES "PUBLIC"."HOMSAI_ENTITY_TYPES"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."HOMSAI_ENTITIES" ADD CONSTRAINT "PUBLIC"."FKDA2MRKPNQRQV396STUU257NYY" FOREIGN KEY("AREA_UUID") REFERENCES "PUBLIC"."HA_AREAS"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."HOMSAI_ENTITIES_HISTORICAL_STATES" ADD CONSTRAINT "PUBLIC"."FKOH16USWX36X5JI159YF7WHIE" FOREIGN KEY("AREA_UUID") REFERENCES "PUBLIC"."HA_AREAS"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."HOMSAI_ENTITIES" ADD CONSTRAINT "PUBLIC"."FKK5P0N7HTMWPP4A0DC10KNW2GX" FOREIGN KEY("AREA_UUID") REFERENCES "PUBLIC"."HA_AREAS"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."HOMSAI_ENTITIES" ADD CONSTRAINT "PUBLIC"."FKCF4BDLGRS7LE1XLOSYATLUG5M" FOREIGN KEY("TYPE_UUID") REFERENCES "PUBLIC"."HOMSAI_ENTITY_TYPES"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."ENTITIES_AREAS" ADD CONSTRAINT "PUBLIC"."FKQ2B6CMTP0REMESXW4WLD3M7YR" FOREIGN KEY("AREA_UUID") REFERENCES "PUBLIC"."HA_AREAS"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."ENTITIES_AREAS" ADD CONSTRAINT "PUBLIC"."FKPPR3CXSRLMVH4G0XT03P5DQYU" FOREIGN KEY("ENTITY_UUID") REFERENCES "PUBLIC"."HA_ENTITIES"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."HA_ENTITIES_HOMSAI_ENTITIES" ADD CONSTRAINT "PUBLIC"."FKHRWP5M4TOIT00TBX2HO9ER1CL" FOREIGN KEY("HOMSAI_ENTIITY_UUID") REFERENCES "PUBLIC"."HOMSAI_ENTITIES"("UUID") NOCHECK;
ALTER TABLE "PUBLIC"."HVAC_DEVICES" ADD CONSTRAINT "PUBLIC"."FKQS4XUQFGF4JPSD3MUWP2H1CR9" FOREIGN KEY("AREA_UUID") REFERENCES "PUBLIC"."HA_AREAS"("UUID") NOCHECK;





INSERT INTO "PUBLIC"."HA_AREAS" (UUID, CREATED_AT, DELETED_AT, ORIGIN_REFERENCE, UPDATED_AT, VERSION, NAME, DESIRED_WINTER_TEMPERATURE, DESIRED_SUMMER_TEMPERATURE) VALUES ('70b14a1b-811c-44e5-9d92-7226683a7ceb', '2022-01-01 00:00:00.000', NULL, NULL, '2022-01-01 00:00:00.000', 0, 'home_homsai', 20.0, 26.0);
INSERT INTO "PUBLIC"."HOMSAI_ENTITY_TYPES" (UUID, CREATED_AT, DELETED_AT, ORIGIN_REFERENCE, UPDATED_AT, VERSION, NAME, ROOT_NAME, UNIT_OF_MEASUREMENT, DEVICE_CLASS, OPERATOR) VALUES ('f13810f8-39cc-4cff-8305-9a29aa660138', '2022-01-01 00:00:00.000', NULL, NULL, '2022-01-01 00:00:00.000', 0, 'AVG_TEMP', 'Average temperature ', '°C', 'temperature', 'AVG');
INSERT INTO "PUBLIC"."HOMSAI_ENTITY_TYPES" (UUID, CREATED_AT, DELETED_AT, ORIGIN_REFERENCE, UPDATED_AT, VERSION, NAME, ROOT_NAME, UNIT_OF_MEASUREMENT, DEVICE_CLASS, OPERATOR) VALUES ('dffbba51-3a6f-4a91-b406-9ef0fca04d99', '2022-01-01 00:00:00.000', NULL, NULL, '2022-01-01 00:00:00.000', 0, 'AVG_HUM', 'Average humidity ', '%', 'humidity', 'AVG');
INSERT INTO "PUBLIC"."HOME_INFO" (UUID, CREATED_AT, UPDATED_AT, VERSION, PHOTOVOLTAIC_OPTIMIZATIONS_ENABLED) VALUES ('4dc3ff72-d861-4d29-855f-fee6d32530b4', '2022-01-01 00:00:00.000', '2022-01-01 00:00:00.000', 0, FALSE);