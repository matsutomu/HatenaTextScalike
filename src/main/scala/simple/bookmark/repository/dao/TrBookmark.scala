package simple.bookmark.repository.dao

import scalikejdbc._
import org.joda.time.{DateTime}

case class TrBookmark(
  id: Long,
  userId: Long,
  entryId: Long,
  comment: Option[String] = None,
  createdTimestamp: DateTime,
  deletedTimestamp: Option[DateTime] = None) {

  def save()(implicit session: DBSession = TrBookmark.autoSession): TrBookmark = TrBookmark.save(this)(session)

  def destroy()(implicit session: DBSession = TrBookmark.autoSession): Unit = TrBookmark.destroy(this)(session)

}


object TrBookmark extends SQLSyntaxSupport[TrBookmark] {

  override val tableName = "TR_BOOKMARK"

  override val columns = Seq("ID", "USER_ID", "ENTRY_ID", "COMMENT", "CREATED_TIMESTAMP", "DELETED_TIMESTAMP")

  def apply(tb: SyntaxProvider[TrBookmark])(rs: WrappedResultSet): TrBookmark = apply(tb.resultName)(rs)
  def apply(tb: ResultName[TrBookmark])(rs: WrappedResultSet): TrBookmark = new TrBookmark(
    id = rs.get(tb.id),
    userId = rs.get(tb.userId),
    entryId = rs.get(tb.entryId),
    comment = rs.get(tb.comment),
    createdTimestamp = rs.get(tb.createdTimestamp),
    deletedTimestamp = rs.get(tb.deletedTimestamp)
  )

  val tb = TrBookmark.syntax("tb")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[TrBookmark] = {
    withSQL {
      select.from(TrBookmark as tb).where.eq(tb.id, id)
    }.map(TrBookmark(tb.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[TrBookmark] = {
    withSQL(select.from(TrBookmark as tb)).map(TrBookmark(tb.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(TrBookmark as tb)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[TrBookmark] = {
    withSQL {
      select.from(TrBookmark as tb).where.append(where)
    }.map(TrBookmark(tb.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[TrBookmark] = {
    withSQL {
      select.from(TrBookmark as tb).where.append(where)
    }.map(TrBookmark(tb.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(TrBookmark as tb).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userId: Long,
    entryId: Long,
    comment: Option[String] = None,
    createdTimestamp: DateTime,
    deletedTimestamp: Option[DateTime] = None)(implicit session: DBSession = autoSession): TrBookmark = {
    val generatedKey = withSQL {
      insert.into(TrBookmark).columns(
        column.userId,
        column.entryId,
        column.comment,
        column.createdTimestamp,
        column.deletedTimestamp
      ).values(
        userId,
        entryId,
        comment,
        createdTimestamp,
        deletedTimestamp
      )
    }.updateAndReturnGeneratedKey.apply()

    TrBookmark(
      id = generatedKey,
      userId = userId,
      entryId = entryId,
      comment = comment,
      createdTimestamp = createdTimestamp,
      deletedTimestamp = deletedTimestamp)
  }

  def batchInsert(entities: Seq[TrBookmark])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'userId -> entity.userId,
        'entryId -> entity.entryId,
        'comment -> entity.comment,
        'createdTimestamp -> entity.createdTimestamp,
        'deletedTimestamp -> entity.deletedTimestamp))
        SQL("""insert into TR_BOOKMARK(
        USER_ID,
        ENTRY_ID,
        COMMENT,
        CREATED_TIMESTAMP,
        DELETED_TIMESTAMP
      ) values (
        {userId},
        {entryId},
        {comment},
        {createdTimestamp},
        {deletedTimestamp}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: TrBookmark)(implicit session: DBSession = autoSession): TrBookmark = {
    withSQL {
      update(TrBookmark).set(
        column.id -> entity.id,
        column.userId -> entity.userId,
        column.entryId -> entity.entryId,
        column.comment -> entity.comment,
        column.createdTimestamp -> entity.createdTimestamp,
        column.deletedTimestamp -> entity.deletedTimestamp
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: TrBookmark)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(TrBookmark).where.eq(column.id, entity.id) }.update.apply()
  }

}
