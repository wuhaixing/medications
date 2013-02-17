    # Drugs schema
     
    # --- !Ups
    
    CREATE SEQUENCE drug_id_seq;
    
    CREATE TABLE drug(
        id integer NOT NULL DEFAULT nextval('drug_id_seq'),
        label varchar(255),
        zhunzi varchar(64),
        company varchar(128),
        basedCode varchar(255)
    );
    
    CREATE TABLE SOCIAL_USER (
        user_id                   varchar(255),
        provider_id               varchar(255),
        first_name                varchar(64),
        last_name                 varchar(64),
        full_name                 varchar(128),
        email                     varchar(255),
        avatar_url                 varchar(255),
        hasher                    varchar(255),
        password                  varchar(255));
                
    CREATE TABLE TOKEN (
        UUID VARCHAR(100) NOT NULL, 
        EMAIL VARCHAR(100), 
        CREATION_TIME TIMESTAMP, 
        EXPIRATION_TIME TIMESTAMP, 
        IS_SIGN_UP BOOLEAN,
        constraint pk_token_uuid primary key (UUID)); 
    # --- !Downs
     
    DROP TABLE drug;
    DROP TABLE SOCIAL_USER;
    DROP TABLE TOKEN;
    DROP SEQUENCE drug_id_seq;
