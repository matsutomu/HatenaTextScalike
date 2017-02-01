package simple.bookmark.cli

import scalikejdbc._
import scalikejdbc.{AutoSession, ConnectionPool, DBSession}
import simple.bookmark.model.{Bookmark, User}
import simple.bookmark.service.BookmarkApp

import scala.sys.process

/**
  * 
  */
object BookmarkCLI {

  def main(args: Array[String]):Unit = {
    try {
      // DBs.setupAll()
      config.DBsWithEnv("development").setupAll()

      sys.env.get("USER") match {
        case Some(userName) =>
          val app = createApp(userName)
          // args not use
          run(app)          
        case None =>
          process.stderr.println("USER environment must be set.")
          sys.exit(1)
      }
    }
    catch
    {
      case e:Exception => println(e)
        
    }finally {
      // DBs.closeAll() // for h2 DB Embedded mode
      config.DBsWithEnv("development").closeAll()
    }
    
  }

  def createApp(userName: String):BookmarkApp = new BookmarkApp(userName)

  def run(app:BookmarkApp):Int = {
    implicit val session = AutoSession
    var commands = List("help") //args.toList
  
    while(commands.size >= 1 && commands(0) != "exit") {
      commands match {
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
      commands = scala.io.StdIn.readLine("Enter command > ").toString.split(" ").toList
    }

    0
  }

  def add(app: BookmarkApp, url:String, comment:String)(implicit session: DBSession = AutoSession) = {
    app.add(url,comment) match {
      case Right(bookmark) =>
        println("bookmarked" + prettifyBookmark(bookmark))
      case Left(error) =>
        process.stderr.println(error.toString())
    }
  }

  def delete(app: BookmarkApp, url:String)(implicit session: DBSession = AutoSession) = {
    app.deleteByUrl(url) match {
      case Right(_) =>
        println(s"Deleted $url.")
      case Left(error) =>
        process.stderr.println(error.toString())
    }
  }

  def list(app: BookmarkApp)(implicit session: DBSession = AutoSession) = {
    println(s"--- ${app.currentUser.name}'s Bookmarks ---")
    app.list().foreach{ bookmark =>
      println(prettifyBookmark(bookmark))

    }
  }

  def help() = {
    process.stderr.println(
      """
        | usage:
        |   add url [comment]
        |   list
        |   delete url
        |   exit
      """.stripMargin
    )
  }


  private[this] def prettifyBookmark(bookmark: Bookmark):String =
    bookmark.entry.title.getOrElse(" 'no title' ") + " ( " + bookmark.entry.url + " ) " + (if(bookmark.comment == "") "" else " " + bookmark.comment)


}
