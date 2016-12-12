package simple.bookmark.repository

import org.specs2.mutable._
import scalikejdbc.specs2.mutable.AutoRollback


class IdentifierSpec extends Specification with settings.DBSettings {




  "Identifier" should {


    "find by primary keys" in new AutoRollback {
      val id = Identifier.generate.getOrElse("")
      println(id)
      val id_size = id.length
      id_size should be_>(0)
    }

  }

}
