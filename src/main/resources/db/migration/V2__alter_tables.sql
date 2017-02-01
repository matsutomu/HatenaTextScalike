
alter table ms_user add LOGINID  varchar(20) after NAME;
alter table ms_user add PASSWORD varchar(20) after LOGINID;
alter table ms_user add updated_timestamp timestamp after deleted_timestamp;
