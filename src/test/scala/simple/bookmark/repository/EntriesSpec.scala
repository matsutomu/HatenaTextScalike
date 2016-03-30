package simple.bookmark.repository

import org.joda.time.DateTime
import org.specs2.mutable._
import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

import simple.bookmark.model.Entry

class EntriesSpec extends Specification  with settings.DBSettings {


  "EntriesSpec" should {

    "1. find by id" in new AutoRollback {
      val retsById = Entries.find(1).getOrElse(new Entry(1," no url ",Some("not found"),null,null))
      println(retsById)
      retsById.url must_==("localhost")
    }


    "2. find by id (not found) " in new AutoRollback {
      val retsById = Entries.find(0).getOrElse(new Entry(1," no url ",Some("not found"),null,null))
      println(retsById)
      retsById.url === (" no url ")
    }

    "3. create By url" in new AutoRollback {
      // create
      val rets = Entries.findOrCreateByUrl("localhost")
      println(rets)
      rets.url === ("localhost")
    }

    "4. find By url" in new AutoRollback {
      // create
      val rets = Entries.createByUrl("www.yahoo.co.jp")

      // find
      val retsById = Entries.findOrCreateByUrl("www.yahoo.co.jp")
      println(retsById)
      retsById.url === ("www.yahoo.co.jp")
    }

    "5. find By id's" in new AutoRollback {
      val listIds = Seq(1l,2l,3l)
      // find
      val retsByIds:Seq[Entry] = Entries.searchByIds(listIds)
      println("[0]:" + retsByIds(0))
      println("[1]:" + retsByIds(1))
      println("[2]:" + retsByIds(2))

      retsByIds(0).title.getOrElse("") === "sample1"
      retsByIds(1).title.getOrElse("") === "sample2"
      retsByIds(2).title.getOrElse("") === "sample3"
    }

    "6. find By url" in new AutoRollback {
      // create
      val rets = Entries.createByUrl("www.yahoo.co.jp")

      // find
      val retsById = Entries.findByUrl("www.yahoo.co.jp")
      println(retsById)
      retsById.getOrElse(new Entry(1," no url ",Some("not found"),null,null)).url === ("www.yahoo.co.jp")
    }


  }

}



