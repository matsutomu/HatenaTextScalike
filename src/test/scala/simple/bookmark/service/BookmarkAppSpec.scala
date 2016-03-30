package simple.bookmark.service

import org.joda.time.DateTime
import org.specs2.mutable._
import scalikejdbc.specs2.mutable.AutoRollback
import simple.bookmark.model._

class BookmarkAppSpec extends Specification  with settings.DBSettings {


  "BookmarkAppSpec" should {

    "1. current user create " in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Bob")
      // create (or Update)
      val user = bookmarkApp.currentUser

      println(user)
      user.name must_== ("Bob")

    }

    "2. current user get " in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Alice")
      // create (or Update)
      val user = bookmarkApp.currentUser

      println(user)
      user.name must_== ("Alice")

    }

    "3. find by id " in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Alice")
      val bookmark = bookmarkApp.find(1)
      println(bookmark)

      bookmark.get.entry.title must_== Some("sample1")
      bookmark.get.entry.url   must_== "localhost"
      bookmark.get.user must_== bookmarkApp.currentUser

    }

    "4. add" in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Alice")
      val bookmark = bookmarkApp.add("yahoo.co.jp","有名なヤフー！です。")

      bookmark match {
        case Left(e)  => println(e)
        case Right(b) => b.entry.url must_== "yahoo.co.jp"
      }

    }

    "5. edit" in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Alice")
      val bookmark = bookmarkApp.edit(1,"有名なヤフー！です。")
      bookmark.right.get.entry.url must_== "localhost"
      bookmark.right.get.comment   must_== "有名なヤフー！です。"

    }

    "6. edit (error )" in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Alice")
      bookmarkApp.edit(1000,"有名なヤフー！です。") must_== Left(BookmarkNotFoundError)

    }


    "7. list " in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Alice")
      val bookmarks = bookmarkApp.list()
      bookmarks.length must_== 1
      bookmarks(0).entry.url must_== ("localhost")

    }


    "8. deleteId " in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Alice")
      bookmarkApp.delete(1) must_== Right(())
    }

    "9. deleteId (no found) " in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Alice")
      bookmarkApp.delete(1000) must_== Left(BookmarkNotFoundError)
    }

    "10. deleteByUrl " in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Alice")
      bookmarkApp.deleteByUrl("localhost") must_== Right(())
    }

    "11. deleteByUrl (not found) " in new AutoRollback {
      val bookmarkApp = new BookmarkApp("Alice")
      bookmarkApp.deleteByUrl("www.yahoo.co.jp") must_== Left(EntryNotFoundError)
    }

  }

}



