package simple.bookmark.repository.dao

import scalikejdbc._
import org.joda.time.{DateTime}

case class TrEntry(
  id: Long,
  url: String,
  title: Option[String] = None,
  createdTimestamp: DateTime,
  deletedTimestamp: Option[DateTime] = None) {

  def save()(implicit session: DBSession = TrEntry.autoSession): TrEntry = TrEntry.save(this)(session)

  def destroy()(implicit session: DBSession = TrEntry.autoSession): Unit = TrEntry.destroy(this)(session)

}


object TrEntry extends SQLSyntaxSupport[TrEntry] {

  override val tableName = "TR_ENTRY"

  override val columns = Seq("ID", "URL", "TITLE", "CREATED_TIMESTAMP", "DELETED_TIMESTAMP")

  def apply(te: SyntaxProvider[TrEntry])(rs: WrappedResultSet): TrEntry = apply(te.resultName)(rs)
  def apply(te: ResultName[TrEntry])(rs: WrappedResultSet): TrEntry = new TrEntry(
    id = rs.get(te.id),
    url = rs.get(te.url),
    title = rs.get(te.title),
    createdTimestamp = rs.get(te.createdTimestamp),
    deletedTimestamp = rs.get(te.deletedTimestamp)
  )

  val te = TrEntry.syntax("te")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[TrEntry] = {
    withSQL {
      select.from(TrEntry as te).where.eq(te.id, id)
    }.map(TrEntry(te.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[TrEntry] = {
    withSQL(select.from(TrEntry as te)).map(TrEntry(te.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(TrEntry as te)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[TrEntry] = {
    withSQL {
      select.from(TrEntry as te).where.append(where)
    }.map(TrEntry(te.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[TrEntry] = {
    withSQL {
      select.from(TrEntry as te).where.append(where)
    }.map(TrEntry(te.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(TrEntry as te).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    url: String,
    title: Option[String] = None,
    createdTimestamp: DateTime,
    deletedTimestamp: Option[DateTime] = None)(implicit session: DBSession = autoSession): TrEntry = {
    val generatedKey = withSQL {
      insert.into(TrEntry).columns(
        column.url,
        column.title,
        column.createdTimestamp,
        column.deletedTimestamp
      ).values(
        url,
        title,
        createdTimestamp,
        deletedTimestamp
      )
    }.updateAndReturnGeneratedKey.apply()

    TrEntry(
      id = generatedKey,
      url = url,
      title = title,
      createdTimestamp = createdTimestamp,
      deletedTimestamp = deletedTimestamp)
  }

  def batchInsert(entities: Seq[TrEntry])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'url -> entity.url,
        'title -> entity.title,
        'createdTimestamp -> entity.createdTimestamp,
        'deletedTimestamp -> entity.deletedTimestamp))
        SQL("""insert into TR_ENTRY(
        URL,
        TITLE,
        CREATED_TIMESTAMP,
        DELETED_TIMESTAMP
      ) values (
        {url},
        {title},
        {createdTimestamp},
        {deletedTimestamp}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: TrEntry)(implicit session: DBSession = autoSession): TrEntry = {
    withSQL {
      update(TrEntry).set(
        column.id -> entity.id,
        column.url -> entity.url,
        column.title -> entity.title,
        column.createdTimestamp -> entity.createdTimestamp,
        column.deletedTimestamp -> entity.deletedTimestamp
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: TrEntry)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(TrEntry).where.eq(column.id, entity.id) }.update.apply()
  }

}
