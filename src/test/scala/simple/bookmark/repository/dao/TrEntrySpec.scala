package simple.bookmark.repository.dao

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class TrEntrySpec extends Specification {

  "TrEntry" should {

    val te = TrEntry.syntax("te")

    "find by primary keys" in new AutoRollback {
      val maybeFound = TrEntry.find(1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = TrEntry.findBy(sqls.eq(te.id, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = TrEntry.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = TrEntry.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = TrEntry.findAllBy(sqls.eq(te.id, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = TrEntry.countBy(sqls.eq(te.id, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = TrEntry.create(url = "MyString", createdTimestamp = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = TrEntry.findAll().head
      // TODO modify something
      val modified = entity
      val updated = TrEntry.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = TrEntry.findAll().head
      TrEntry.destroy(entity)
      val shouldBeNone = TrEntry.find(1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = TrEntry.findAll()
      entities.foreach(e => TrEntry.destroy(e))
      val batchInserted = TrEntry.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
