package simple.bookmark.repository

import org.specs2.mutable._
import scalikejdbc.specs2.mutable.AutoRollback

import simple.bookmark.model.User

class UsersSpec extends Specification with settings.DBSettings {


  "UsersSpec" should {

    "1. find by id" in new AutoRollback {

      val retsById = Users.find(1).getOrElse(new User(1,"not found","", "",null,null))
      retsById.name must beEqualTo("Alice")
    }

    "2. find by id (not found) " in new AutoRollback {
      val retsById = Users.find(0).getOrElse(new User(1,"not found","", "",null,null))
      retsById.name must beEqualTo("not found")
    }

    "3. create By name" in new AutoRollback {
      // create
      val rets = Users.findOrCreateByName("テスト")
      rets.name must beEqualTo("テスト")
    }

    "4. find By name" in new AutoRollback {
      // find
      val retsById = Users.findOrCreateByName("Alice")
      retsById.name  must beEqualTo("Alice")
    }

    "5. find By Logind" in new AutoRollback {
      // find
      val retsById = Users.findByLoginId("Alice").getOrElse(new User(1,"not found","", "",null,null))
      retsById.name  must beEqualTo("Alice")
    }

  }

}


