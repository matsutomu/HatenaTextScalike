package simple.bookmark.repository


import java.time.LocalDateTime

import org.joda.time.DateTime
import scalikejdbc._
import simple.bookmark.model._
import simple.bookmark.repository.dao._

/**
  * Created by matsutomu on 16/02/17.
  */
object Bookmarks {

  val defDateTime = new DateTime(1970,1,1,1,1)



  // why not retun Bookmark
  def createOrUpdate(user:User,  entry: Entry, comment:String)(implicit session: DBSession = AutoSession):Unit = {
    findByEntry(user,entry) match {
      case Some(b) =>
        val updateAt = DateTime.now()

        TrBookmark.save(new TrBookmark(b.id,b.user.id,b.entry.id,Some(comment),updateAt,None))
      case None =>
        sql"""
              insert into TR_BOOKMARK
                (user_id, entry_id, comment, created_timestamp)
                values
                (${user.id}, ${entry.id},${comment}, CURRENT_TIMESTAMP())
          """.execute().apply()

    }
  }

  def delete(bookmark: Bookmark)(implicit session: DBSession = AutoSession):Unit = {
    sql" DELETE FROM TR_BOOKMARK WHERE ID = ${bookmark.id}".execute().apply()
  }

  def findByEntry(user:User, entry:Entry)(implicit session: DBSession = AutoSession):Option[Bookmark] = {
    val sqlWhere = sqls" USER_ID = ${user.id} AND ENTRY_ID = ${entry.id}"
    val trBookmark = TrBookmark.findBy(sqlWhere)
    trBookmark match {
      case Some(e) => Some(toBookmark(e, user, entry))
      case None => None
    }
  }

  def find(bookmarkId: Long)(implicit session: DBSession = AutoSession):Option[Bookmark] = for{
    trBookmark <- TrBookmark.find(bookmarkId)
    entry <- Entries.find(trBookmark.entryId)
    user  <- Users.find(trBookmark.userId)
  }yield toBookmark(trBookmark,user,entry)


  def listAll(user: User)(implicit session: DBSession = AutoSession): Seq[Bookmark] = {
    val sWhere = sqls" user_id = ${user.id} "
    val sOrder = sqls" created_timestamp desc, id desc "

    val lst:List[TrBookmark] = withSQL {
        select.from(TrBookmark as TrBookmark.tb).where.append(sWhere).orderBy(sOrder)
      }.map(TrBookmark(TrBookmark.tb.resultName)).list.apply()

    for {
      tr <- lst
      ent <- Entries.find(tr.entryId)
    } yield toBookmark(tr, user, ent)

  }




  def toBookmark(p:TrBookmark,user:User, entry:Entry):Bookmark = {
    new Bookmark(p.id,user,entry,p.comment.getOrElse("")
          , p.createdTimestamp.toLocalDateTime
          , p.deletedTimestamp.getOrElse(defDateTime).toLocalDateTime)
  }


}
