package simple.bookmark.repository.dao

import scalikejdbc._
import org.joda.time.{DateTime}

case class MsUser(
  id: Long,
  name: String,
  createdTimestamp: DateTime,
  deletedTimestamp: Option[DateTime] = None) {

  def save()(implicit session: DBSession = MsUser.autoSession): MsUser = MsUser.save(this)(session)

  def destroy()(implicit session: DBSession = MsUser.autoSession): Unit = MsUser.destroy(this)(session)

}


object MsUser extends SQLSyntaxSupport[MsUser] {

  override val tableName = "MS_USER"

  override val columns = Seq("ID", "NAME", "CREATED_TIMESTAMP", "DELETED_TIMESTAMP")

  def apply(mu: SyntaxProvider[MsUser])(rs: WrappedResultSet): MsUser = apply(mu.resultName)(rs)
  def apply(mu: ResultName[MsUser])(rs: WrappedResultSet): MsUser = new MsUser(
    id = rs.get(mu.id),
    name = rs.get(mu.name),
    createdTimestamp = rs.get(mu.createdTimestamp),
    deletedTimestamp = rs.get(mu.deletedTimestamp)
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
    createdTimestamp: DateTime,
    deletedTimestamp: Option[DateTime] = None)(implicit session: DBSession = autoSession): MsUser = {
    val generatedKey = withSQL {
      insert.into(MsUser).columns(
        column.name,
        column.createdTimestamp,
        column.deletedTimestamp
      ).values(
        name,
        createdTimestamp,
        deletedTimestamp
      )
    }.updateAndReturnGeneratedKey.apply()

    MsUser(
      id = generatedKey,
      name = name,
      createdTimestamp = createdTimestamp,
      deletedTimestamp = deletedTimestamp)
  }

  def batchInsert(entities: Seq[MsUser])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name,
        'createdTimestamp -> entity.createdTimestamp,
        'deletedTimestamp -> entity.deletedTimestamp))
        SQL("""insert into MS_USER(
        NAME,
        CREATED_TIMESTAMP,
        DELETED_TIMESTAMP
      ) values (
        {name},
        {createdTimestamp},
        {deletedTimestamp}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: MsUser)(implicit session: DBSession = autoSession): MsUser = {
    withSQL {
      update(MsUser).set(
        column.id -> entity.id,
        column.name -> entity.name,
        column.createdTimestamp -> entity.createdTimestamp,
        column.deletedTimestamp -> entity.deletedTimestamp
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: MsUser)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(MsUser).where.eq(column.id, entity.id) }.update.apply()
  }

}
