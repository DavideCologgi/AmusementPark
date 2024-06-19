-- Drop existing schema if it exists
DROP SCHEMA IF EXISTS amusementparkdb CASCADE;

-- Create new schema
CREATE SCHEMA amusementparkdb AUTHORIZATION postgres;

-- Drop sequence if it exists
DROP SEQUENCE IF EXISTS amusementparkdb.ticket_counter_id_seq;

-- Create new sequence
CREATE SEQUENCE amusementparkdb.ticket_counter_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

-- Drop table if it exists
DROP TABLE IF EXISTS amusementparkdb.attractions;

-- Create attractions table
CREATE TABLE amusementparkdb.attractions (
    id_attraction uuid DEFAULT gen_random_uuid() NOT NULL,
    "name" varchar(255) NOT NULL,
    description varchar(255),
    ticket_duration int4 NOT NULL,
    max_ticket int4 NOT NULL,
    CONSTRAINT attractions_pk PRIMARY KEY (id_attraction),
    CONSTRAINT attractions_unique UNIQUE ("name")
);

-- Drop table if it exists
DROP TABLE IF EXISTS amusementparkdb.ticket_counter;

-- Create ticket_counter table
CREATE TABLE amusementparkdb.ticket_counter (
    id bigserial NOT NULL,
    counter int8 NOT NULL,
    CONSTRAINT ticket_counter_pkey PRIMARY KEY (id)
);

-- Drop table if it exists
DROP TABLE IF EXISTS amusementparkdb.users;

-- Create users table
CREATE TABLE amusementparkdb.users (
    id_user uuid DEFAULT gen_random_uuid() NOT NULL,
    "name" varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id_user),
    CONSTRAINT users_unique UNIQUE (email)
);

-- Drop table if it exists
DROP TABLE IF EXISTS amusementparkdb.tickets;

-- Create tickets table
CREATE TABLE amusementparkdb.tickets (
    id_ticket uuid DEFAULT gen_random_uuid() NOT NULL,
    id_attraction uuid NOT NULL,
    id_user uuid,
    "number" int8 NOT NULL,
    emission_date timestamp(6) NOT NULL,
    expiration_date timestamp(6) NOT NULL,
    calling_date timestamp(6),
    CONSTRAINT tickets_pk PRIMARY KEY (id_ticket),
    CONSTRAINT tickets_unique_2 UNIQUE ("number"),
    CONSTRAINT fk_attraction FOREIGN KEY (id_attraction) REFERENCES amusementparkdb.attractions(id_attraction),
    CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES amusementparkdb.users(id_user)
);

-- Drop and recreate UUID functions if they exist
DROP FUNCTION IF EXISTS amusementparkdb.uuid_generate_v1;
CREATE OR REPLACE FUNCTION amusementparkdb.uuid_generate_v1()
RETURNS uuid
LANGUAGE c
PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v1$function$;

DROP FUNCTION IF EXISTS amusementparkdb.uuid_generate_v1mc;
CREATE OR REPLACE FUNCTION amusementparkdb.uuid_generate_v1mc()
RETURNS uuid
LANGUAGE c
PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v1mc$function$;

DROP FUNCTION IF EXISTS amusementparkdb.uuid_generate_v3;
CREATE OR REPLACE FUNCTION amusementparkdb.uuid_generate_v3(namespace uuid, name text)
RETURNS uuid
LANGUAGE c
IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v3$function$;

DROP FUNCTION IF EXISTS amusementparkdb.uuid_generate_v4;
CREATE OR REPLACE FUNCTION amusementparkdb.uuid_generate_v4()
RETURNS uuid
LANGUAGE c
PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v4$function$;

DROP FUNCTION IF EXISTS amusementparkdb.uuid_generate_v5;
CREATE OR REPLACE FUNCTION amusementparkdb.uuid_generate_v5(namespace uuid, name text)
RETURNS uuid
LANGUAGE c
IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v5$function$;

DROP FUNCTION IF EXISTS amusementparkdb.uuid_nil;
CREATE OR REPLACE FUNCTION amusementparkdb.uuid_nil()
RETURNS uuid
LANGUAGE c
IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_nil$function$;

DROP FUNCTION IF EXISTS amusementparkdb.uuid_ns_dns;
CREATE OR REPLACE FUNCTION amusementparkdb.uuid_ns_dns()
RETURNS uuid
LANGUAGE c
IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_ns_dns$function$;

DROP FUNCTION IF EXISTS amusementparkdb.uuid_ns_oid;
CREATE OR REPLACE FUNCTION amusementparkdb.uuid_ns_oid()
RETURNS uuid
LANGUAGE c
IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_ns_oid$function$;

DROP FUNCTION IF EXISTS amusementparkdb.uuid_ns_url;
CREATE OR REPLACE FUNCTION amusementparkdb.uuid_ns_url()
RETURNS uuid
LANGUAGE c
IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_ns_url$function$;

DROP FUNCTION IF EXISTS amusementparkdb.uuid_ns_x500;
CREATE OR REPLACE FUNCTION amusementparkdb.uuid_ns_x500()
RETURNS uuid
LANGUAGE c
IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_ns_x500$function$;
