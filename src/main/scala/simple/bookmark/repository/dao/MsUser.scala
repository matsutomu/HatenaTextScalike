package simple.bookmark.repository.dao

import scalikejdbc._
import org.joda.time.{DateTime}

case class MsUser(
  id: Long,
  name: String,
  loginid : String,
  password: String,
  createdTimestamp: DateTime,
  deletedTimestamp: Option[DateTime] = None,
  updatedTimestamp: Option[DateTime] = None) {

  def save()(implicit session: DBSession = MsUser.autoSession): MsUser = MsUser.save(this)(session)

  def destroy()(implicit session: DBSession = MsUser.autoSession): Unit = MsUser.destroy(this)(session)

}


object MsUser extends SQLSyntaxSupport[MsUser] {

  override val tableName = "MS_USER"

  override val columns = Seq("ID", "NAME", "LOGINID", "PASSWORD" , "CREATED_TIMESTAMP", "DELETED_TIMESTAMP", "UPDATED_TIMESTAMP")

  def apply(mu: SyntaxProvider[MsUser])(rs: WrappedResultSet): MsUser = apply(mu.resultName)(rs)
  def apply(mu: ResultName[MsUser])(rs: WrappedResultSet): MsUser = new MsUser(
    id = rs.get(mu.id),
    name = rs.get(mu.name),
    loginid = rs.get(mu.loginid),
    password = rs.get(mu.password),
    createdTimestamp = rs.get(mu.createdTimestamp),
    deletedTimestamp = rs.get(mu.deletedTimestamp),
    updatedTimestamp = rs.get(mu.updatedTimestamp)
  )

  val mu = MsUser.syntax("mu")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[MsUser] = {
    withSQL {
      select.from(MsUser as mu).where.eq(mu.id, id)
    }.map(MsUser(mu.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[MsUser] = {
    withSQL(select.from(MsUser as mu)).map(MsUser(mu.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(MsUser as mu)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[MsUser] = {
    withSQL {
      select.from(MsUser as mu).where.append(where)
    }.map(MsUser(mu.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[MsUser] = {
    withSQL {
      select.from(MsUser as mu).where.append(where)
    }.map(MsUser(mu.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(MsUser as mu).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: String,
    loginid: String,
    password: String,
    createdTimestamp: DateTime,
    deletedTimestamp: Option[DateTime] = None,
    updatedTimestamp: Option[DateTime] = None)(implicit session: DBSession = autoSession): MsUser = {
    val generatedKey = withSQL {
      insert.into(MsUser).columns(
        column.name,
        column.loginid,
        column.password,
        column.createdTimestamp,
        column.deletedTimestamp,
        column.updatedTimestamp
      ).values(
        name,
        loginid,
        password,
        createdTimestamp,
        deletedTimestamp,
        updatedTimestamp
      )
    }.updateAndReturnGeneratedKey.apply()

    MsUser(
      id = generatedKey,
      name = name,
      loginid = loginid,
      password = password,
      createdTimestamp = createdTimestamp,
      deletedTimestamp = deletedTimestamp,
      updatedTimestamp = updatedTimestamp)
  }

  def batchInsert(entities: Seq[MsUser])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name,
        'loginid -> entity.loginid,
        'password -> entity.password,
        'createdTimestamp -> entity.createdTimestamp,
        'deletedTimestamp -> entity.deletedTimestamp,
        'updatedTimestamp -> entity.updatedTimestamp))
        SQL("""insert into MS_USER(
        NAME,
        LOGINID,
        PASSWORD,
        CREATED_TIMESTAMP,
        DELETED_TIMESTAMP,
        UPDATED_TIMESTAMP
      ) values (
        {name},
        {loginid},
        {password},
        {createdTimestamp},
        {deletedTimestamp},
        {updatedTimestamp}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: MsUser)(implicit session: DBSession = autoSession): MsUser = {
    withSQL {
      update(MsUser).set(
        column.id -> entity.id,
        column.name -> entity.name,
        column.loginid -> entity.loginid,
        column.password -> entity.password,
        column.createdTimestamp -> entity.createdTimestamp,
        column.deletedTimestamp -> entity.deletedTimestamp,
        column.updatedTimestamp -> entity.updatedTimestamp
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: MsUser)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(MsUser).where.eq(column.id, entity.id) }.update.apply()
  }

}
