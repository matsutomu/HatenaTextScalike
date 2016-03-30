package simple.bookmark.repository

import org.joda.time.DateTime
import scalikejdbc._
import simple.bookmark.model.{Bookmark, Entry}
import simple.bookmark.repository.dao.{TrEntry}

/**
  * Created by tsutomu on 16/02/14.
  */
object Entries {

  val defDateTime = new DateTime(1970,1,1,1,1)

  def find(entryId: Long)(implicit session: DBSession = AutoSession): Option[Entry] =
    toEntry(TrEntry.find(entryId))

  def findByUrl(url: String)(implicit session: DBSession = AutoSession): Option[Entry] = {
    val whereName = sqls" url = ${url} "
    toEntry(TrEntry.findBy(whereName))
  }

  def findOrCreateByUrl(url:String)(implicit session: DBSession = AutoSession): Entry =
    findByUrl(url).getOrElse(createByUrl(url))


  def createByUrl(url:String)(implicit session: DBSession = AutoSession): Entry = {

    val trEntry = TrEntry.create(url, None, DateTime.now(), null)

    new Entry(trEntry.id, trEntry.url, trEntry.title
              ,trEntry.createdTimestamp.toLocalDateTime,None)

  }

  def deleteById(entryId: Long)(implicit session: DBSession = AutoSession):Unit = {
    sql" DELETE FROM TR_ENTRY WHERE ID = ${entryId}".execute().apply()
  }

  def searchByIds(ids: Seq[Long])(implicit session: DBSession = AutoSession):Seq[Entry] = {
    val whereIds = sqls" id in ( ${ids} ) "
    val trEntries = TrEntry.findAllBy(whereIds)
    // How may such a description ?
    // None be sure not returned
    trEntries.map(x => toEntry(Some(x)).filter(_ != None).get).toSeq
  }

  private def toEntry(pTrEntry: Option[TrEntry]):Option[Entry] = {
    pTrEntry match {
      case Some(m) => {
        Some(new Entry(m.id, m.url, m.title
          , m.createdTimestamp.toLocalDateTime
          , Some(m.deletedTimestamp.getOrElse(defDateTime).toLocalDateTime)))
      }
      case None => None
    }
  }

}
