package simple.bookmark.repository

import org.specs2.mutable._
import scalikejdbc.specs2.mutable.AutoRollback
import org.joda.time.DateTime
import simple.bookmark.model.User
import simple.bookmark.model.Entry
import simple.bookmark.model.Bookmark

class BookmarksSpec extends Specification  with settings.DBSettings {

  "BookmarksSpec" should {

      "1. find by id" in new AutoRollback {
        val testUser = new User(1,"Test", "", "", DateTime.now().toLocalDateTime, Some(DateTime.now().toLocalDateTime))
        val testEntry = new Entry(1, "localhost",Some("") ,DateTime.now().toLocalDateTime, Some(DateTime.now().toLocalDateTime))
        val retsById = Bookmarks.find(1).getOrElse(new Bookmark(1,testUser,testEntry,"",DateTime.now().toLocalDateTime,DateTime.now.toLocalDateTime))

        println(retsById)
        retsById.comment must_==("テスト entry 1")
      }

      "2. find by id (not found) " in new AutoRollback {
        val testUser = new User(1,"Test","", "", DateTime.now().toLocalDateTime, Some(DateTime.now().toLocalDateTime))
        val testEntry = new Entry(1, "localhost",Some("") ,DateTime.now().toLocalDateTime, Some(DateTime.now().toLocalDateTime))
        val retsById = Bookmarks.find(0).getOrElse(
                                new Bookmark(0, testUser, testEntry ,"not found", DateTime.now().toLocalDateTime, null))
        println(retsById)
        retsById.comment must_== ("not found")
      }

      "3. create By User, Entry" in new AutoRollback {
        // create
        val testUser = Users.find(1).get
        val testEntry = Entries.find(2).get
        Bookmarks.createOrUpdate(testUser,testEntry,"create!")

        val rets = Bookmarks.findByEntry(testUser,testEntry)

        rets.get.comment must_== ("create!")
      }

      "4. del by BookmarkId" in new AutoRollback {
        val testUser = new User(1,"Test","", "", DateTime.now().toLocalDateTime, Some(DateTime.now().toLocalDateTime))
        val testEntry = new Entry(1, "localhost",Some("") ,DateTime.now().toLocalDateTime, Some(DateTime.now().toLocalDateTime))
        val retsById = Bookmarks.find(1).getOrElse(new Bookmark(1,testUser,testEntry,"",DateTime.now().toLocalDateTime,DateTime.now.toLocalDateTime()))

        // create
        Bookmarks.delete(retsById)

      }

      "5. find By id's" in new AutoRollback {

        val alice = Users.find(1).get
        Bookmarks.createOrUpdate(alice,
                                 Entries.find(1).get,
                                 " sample1 create!")

        Bookmarks.createOrUpdate(alice,
                                 Entries.find(2).get,
                                 " sample2 create!")

        Bookmarks.createOrUpdate(alice,
                                 Entries.find(3).get,
                                 " sample3 create!")

        Bookmarks.createOrUpdate(alice,
                                 Entries.createByUrl("www.google.com"),
                                  " sample4 create!")

        Bookmarks.createOrUpdate(Users.findOrCreateByName("Bob"),
                                 Entries.find(2).get,
                                 " Bob's sample2!")

        Entries.deleteById(2)

        val bookmLst = Bookmarks.listAll(alice)
        // bookmLst.foreach(println)

        bookmLst(0).comment must_== " sample4 create!"
        bookmLst(1).comment must_== " sample3 create!"
        bookmLst(2).comment must_== " sample1 create!"
      }

    "6. find By id's pages" in new AutoRollback {

      val alice = Users.find(1).get
      Bookmarks.createOrUpdate(alice,
        Entries.find(1).get,
        " sample1 create!")

      Bookmarks.createOrUpdate(alice,
        Entries.find(2).get,
        " sample2 create!")

      Bookmarks.createOrUpdate(alice,
        Entries.find(3).get,
        " sample3 create!")

      Bookmarks.createOrUpdate(alice,
        Entries.createByUrl("www.google.com"),
        " sample4 create!")

      // page 3
      Bookmarks.createOrUpdate(alice,
        Entries.createByUrl("www.yhoo.co.jp"),
        " sample5 create!")

      // page 2
      Bookmarks.createOrUpdate(alice,
        Entries.createByUrl("https://www.amazon.co.jp/"),
        " sample6 create!")

      // page 1
      Bookmarks.createOrUpdate(alice,
        Entries.createByUrl("https://www.google.co.jp/"),
        " sample7 create!")

      // page 0
      Bookmarks.createOrUpdate(alice,
        Entries.createByUrl("http://www.yomiuri.co.jp/"),
        " sample4 create!")

      Bookmarks.createOrUpdate(Users.findOrCreateByName("Bob"),
        Entries.find(2).get,
        " Bob's sample2!")

      Entries.deleteById(2)

      val bookmLst = Bookmarks.listPaged(alice,3,3)
      //bookmLst.foreach(println)

      bookmLst(0).comment must_== " sample5 create!"
      bookmLst(1).comment must_== " sample4 create!"
      bookmLst(2).comment must_== " sample3 create!"
      bookmLst.length must_== 3
    }

    "7. count" in new AutoRollback {

      val alice = Users.find(1).get
      Bookmarks.createOrUpdate(alice,
        Entries.find(1).get,
        " sample1 create!")

      Bookmarks.createOrUpdate(alice,
        Entries.find(2).get,
        " sample2 create!")

      Bookmarks.createOrUpdate(alice,
        Entries.find(3).get,
        " sample3 create!")

      val lstCount = Bookmarks.listCount(alice)
      // bookmLst.foreach(println)

      lstCount must_== 3
    }


  }

}



