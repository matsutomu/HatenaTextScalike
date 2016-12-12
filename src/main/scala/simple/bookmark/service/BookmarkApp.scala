package simple.bookmark.service

import scalikejdbc.{AutoSession, DBSession}
import simple.bookmark.repository._
import simple.bookmark.model._


/**
  * Created by matsutomu on 16/02/24.
  */
class BookmarkApp(currentUserName: String) {

  def currentUser(implicit session: DBSession = AutoSession):User = {
    Users.findOrCreateByName(currentUserName)
  }

  def find(bookmarkId:Long)(implicit session: DBSession = AutoSession): Option[Bookmark] = {
    Bookmarks.find(bookmarkId)
  }


  def add(url:String, comment:String)(implicit session: DBSession = AutoSession):Either[Error,Bookmark] = {
    val entry = Entries.findOrCreateByUrl(url)
    val user  = currentUser
    Bookmarks.createOrUpdate(user,entry,comment)
    Bookmarks.findByEntry(user,entry).toRight(BookmarkNotFoundError)
  }

  def edit(bookmarkId: Long, comment:String)(implicit session: DBSession = AutoSession):Either[Error,Bookmark] = {
    for{
      bookmark <- Bookmarks.find(bookmarkId).toRight(BookmarkNotFoundError).right
      _ <- Right(Bookmarks.createOrUpdate(currentUser, bookmark.entry, comment)).right
      editedBookmark <- Bookmarks.find(bookmarkId).toRight(BookmarkNotFoundError).right
    } yield editedBookmark
  }

  def count()(implicit session: DBSession = AutoSession):Long =
    Bookmarks.listCount(currentUser)

  def list()(implicit session: DBSession = AutoSession):List[Bookmark] =
    Bookmarks.listAll(currentUser).toList

  def listPaged(page:Int , limit:Int)(implicit session: DBSession = AutoSession):List[Bookmark] =
    Bookmarks.listPaged(currentUser, page, limit).toList

  def delete(bookmarkId: Long)(implicit session: DBSession = AutoSession): Either[Error, Unit] = {
    for {
      bookmark <- Bookmarks.find(bookmarkId).toRight(BookmarkNotFoundError).right
    } yield Bookmarks.delete(bookmark)
  }

  def deleteByUrl(url: String)(implicit session: DBSession = AutoSession): Either[Error, Unit] = {
    for {
      entry <- Entries.findByUrl(url).toRight(EntryNotFoundError).right
      bookmark <- Bookmarks.findByEntry(currentUser, entry).toRight(BookmarkNotFoundError).right
    } yield Bookmarks.delete(bookmark)
  }




}
