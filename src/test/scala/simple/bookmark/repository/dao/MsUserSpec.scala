package simple.bookmark.repository.dao

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class MsUserSpec extends Specification with settings.DBSettings {

  "MsUser" should {

    val mu = MsUser.syntax("mu")

    "find by primary keys" in new AutoRollback {
      val maybeFound = MsUser.find(1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = MsUser.findBy(sqls.eq(mu.id, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = MsUser.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = MsUser.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = MsUser.findAllBy(sqls.eq(mu.id, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = MsUser.countBy(sqls.eq(mu.id, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = MsUser.create(name = "MyString", loginid="MyString", password="test", createdTimestamp = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = MsUser.findAll().head
      val modified = entity.copy(name = "modified")
      val updated = MsUser.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = MsUser.findAll().head
      MsUser.destroy(entity)
      val shouldBeNone = MsUser.find(1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = MsUser.findAll()
      entities.foreach(e => MsUser.destroy(e))
      val batchInserted = MsUser.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
