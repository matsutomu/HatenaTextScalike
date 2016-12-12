package settings

import org.joda.time.DateTime
import scalikejdbc._, config._

/**
  * Created by matsutomu on 16/02/14.
  */
trait DBSettings {
  DBSettings.initialize()
}

object DBSettings {
  private var isInitialized = false

  def initialize(): Unit = this.synchronized {
    if(isInitialized) return
    println("*** start initialize")
    config.DBsWithEnv("development").setupAll()
    GlobalSettings.loggingSQLErrors = true // error
    GlobalSettings.sqlFormatter = SQLFormatterSettings("utils.HibernateSQLFormatter")
    DBIntializer.run()
    isInitialized = true

    println("*** end initialize")
  }
}


object DBIntializer {

  def run(): Unit = {
    DB readOnly { implicit s =>
      try{
        sql"select 1 from ms_check_fortest limit 1".map(_.long(1)).single.apply()
      }catch{
        case e:java.sql.SQLException =>

          println("*** start create table / insert test data ")
          DB autoCommit { implicit s =>
            sql"""
                create table if not exists ms_user (
                    ID bigint not null auto_increment,
                    NAME varchar(100) not null,
                    created_timestamp timestamp not null,
                    deleted_timestamp timestamp
                );
                alter table ms_user drop constraint if exists pk_ms_user;
                alter table ms_user add  constraint pk_ms_user PRIMARY KEY(ID);
                create unique index if not exists  idx_ms_user_name on ms_user (name);

                create table if not exists tr_entry (
                    ID bigint not null auto_increment,
                    URL varchar(255) not null,
                    title varchar(255),
                    created_timestamp timestamp not null,
                    deleted_timestamp timestamp
                );
                alter table tr_entry drop constraint if exists pk_tr_entry;
                alter table tr_entry add  constraint pk_tr_entry PRIMARY KEY(ID);
                create unique index if not exists idx_tr_entry_url on tr_entry (URL);

                create table if not exists tr_bookmark (
                    ID bigint not null auto_increment,
                    USER_ID bigint not null,
                    ENTRY_ID bigint not null,
                    COMMENT varchar(255),
                    created_timestamp timestamp not null,
                    deleted_timestamp timestamp
                );
                alter table tr_bookmark drop constraint if exists pk_tr_bookmark;
                alter table tr_bookmark add  constraint pk_tr_bookmark PRIMARY KEY(ID);
                create unique index if not exists idx_tr_bookmark on tr_bookmark(USER_ID, ENTRY_ID);

                DROP ALIAS IF EXISTS UUID;
                CREATE ALIAS UUID FOR "org.h2.value.ValueUuid.getNewRandom";

               """.execute.apply()

            // ${DateTime.now} do not use.
            sql"""
                alter table ms_user alter column id restart with 1;
                insert into ms_user (id, name, created_timestamp) values (1, 'Alice', CURRENT_TIMESTAMP());

                alter table tr_entry alter column id restart with 1 ;
                insert into tr_entry ( url, title, created_timestamp) values ('localhost', 'sample1' , CURRENT_TIMESTAMP());
                insert into tr_entry ( url, title, created_timestamp) values ('localhost2', 'sample2' , CURRENT_TIMESTAMP());
                insert into tr_entry ( url, title, created_timestamp) values ('localhost3', 'sample3' , CURRENT_TIMESTAMP());

                alter table tr_bookmark alter column id restart with 1 ;
                insert into tr_bookmark (user_id,entry_id,comment, created_timestamp) values (1, 1, 'テスト entry 1', CURRENT_TIMESTAMP());

                create table if not exists ms_check_fortest (
                    ID bigint not null ,
                    created_timestamp timestamp not null,
                    deleted_timestamp timestamp
                );

            """.execute.apply()
            println("*** end create table / insert test data ")
          }
      }

    }
  }

}
