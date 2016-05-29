package simple.bookmark.cli

import scalikejdbc.config.DBs
import scalikejdbc.{ConnectionPool, AutoSession, DBSession}
import simple.bookmark.model.Bookmark
import simple.bookmark.service.BookmarkApp

import scala.sys.process

/**
  * Created by matsutomu on 16/02/03.
  */
object BookmarkCLI {

  def main(args: Array[String]):Unit = {
    val exitStatus = run(args)
    sys.exit(exitStatus)
  }

  def createApp(userName: String):BookmarkApp = new BookmarkApp(userName)

  def run(args:Array[String]):Int = {
    sys.env.get("USER") match {
      case Some(userName) =>
        try{

          DBs.setupAll()

          implicit val session = AutoSession


          val app = createApp(userName)
          args.toList match {
            case "add" :: url :: rest =>
              val comment = rest.headOption.getOrElse("")
              add(app, url, comment)
            case "delete" :: url :: _ =>
              delete(app, url)
            case "list" :: _ =>
              list(app)
            case _ =>
              help()
          }
        }finally {
          DBs.closeAll() // for h2 DB Embedded mode
        }
      case None =>
        process.stderr.println("USER environment must be set.")
        1
    }
  }

  def add(app: BookmarkApp, url:String, comment:String)(implicit session: DBSession = AutoSession):Int = {
    app.add(url,comment) match {
      case Right(bookmark) =>
        println("bookmarked" + prettifyBookmark(bookmark))
        0
      case Left(error) =>
        process.stderr.println(error.toString())
        1
    }
  }

  def delete(app: BookmarkApp, url:String)(implicit session: DBSession = AutoSession):Int = {
    app.deleteByUrl(url) match {
      case Right(_) =>
        println(s"Deleted $url.")
        0
      case Left(error) =>
        process.stderr.println(error.toString())
        1
    }
  }

  def list(app: BookmarkApp)(implicit session: DBSession = AutoSession):Int = {
    println(s"--- ${app.currentUser.name}'s Bookmarks ---")
    app.list().foreach{ bookmark =>
      println(prettifyBookmark(bookmark))

    }
    0
  }

  def help():Int = {
    process.stderr.println(
      """
        | usage:
        |   run add url [comment]
        |   run list
        |   run delete url
      """.stripMargin
    )
    0
  }


  private[this] def prettifyBookmark(bookmark: Bookmark):String =
    bookmark.entry.title + " ( " + bookmark.entry.url + " ) " + (if(bookmark.comment == "") "" else " " + bookmark.comment)


}
