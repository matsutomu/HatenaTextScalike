package simple.bookmark.repository.dao

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class TrBookmarkSpec extends Specification {

  "TrBookmark" should {

    val tb = TrBookmark.syntax("tb")

    "find by primary keys" in new AutoRollback {
      val maybeFound = TrBookmark.find(1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = TrBookmark.findBy(sqls.eq(tb.id, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = TrBookmark.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = TrBookmark.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = TrBookmark.findAllBy(sqls.eq(tb.id, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = TrBookmark.countBy(sqls.eq(tb.id, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = TrBookmark.create(userId = 1L, entryId = 1L, createdTimestamp = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = TrBookmark.findAll().head
      // TODO modify something
      val modified = entity
      val updated = TrBookmark.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = TrBookmark.findAll().head
      TrBookmark.destroy(entity)
      val shouldBeNone = TrBookmark.find(1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = TrBookmark.findAll()
      entities.foreach(e => TrBookmark.destroy(e))
      val batchInserted = TrBookmark.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
