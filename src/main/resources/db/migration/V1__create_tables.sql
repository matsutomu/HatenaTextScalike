create table if not exists ms_user (
    ID bigint not null auto_increment,
    NAME varchar(100) not null,
    created_timestamp timestamp not null,
    deleted_timestamp timestamp
);

alter table ms_user add constraint pk_ms_user PRIMARY KEY(ID);
create unique index if not exists  idx_ms_user_name on ms_user (name);

create table if not exists tr_entry (
    ID bigint not null auto_increment,
    URL varchar(255) not null,
    title varchar(255),
    created_timestamp timestamp not null,
    deleted_timestamp timestamp
);
alter table tr_entry add constraint pk_tr_entry PRIMARY KEY(ID);
create unique index if not exists idx_tr_entry_url on tr_entry (URL);

create table if not exists tr_bookmark (
    ID bigint not null auto_increment,
    USER_ID bigint not null,
    ENTRY_ID bigint not null,
    COMMENT varchar(255),
    created_timestamp timestamp not null,
    deleted_timestamp timestamp
);
alter table tr_bookmark add constraint pk_tr_bookmark PRIMARY KEY(ID);
create unique index if not exists idx_tr_bookmark on tr_bookmark(USER_ID, ENTRY_ID);



CREATE ALIAS UUID FOR "org.h2.value.ValueUuid.getNewRandom";


create table if not exists ms_check_fortest (
    ID bigint not null ,
    created_timestamp timestamp not null,
    deleted_timestamp timestamp
);
