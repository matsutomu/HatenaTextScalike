package simple.bookmark.repository

import scalikejdbc._
import scalikejdbc.{AutoSession, DBSession}

import simple.bookmark.repository.dao.MsUser
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import simple.bookmark.model.User

/**
  * Created by matsutomu on 16/02/12.
  */
object Users {

  val defDateTime = new DateTime(1970,1,1,1,1)


  def findByLoginId(loginId: String)(implicit session: DBSession = AutoSession): Option[User] = {
    val sqlWhere = sqls" LOGINID = ${loginId} "
    toUser(MsUser.findBy(sqlWhere ))
  }

  def find(userId: Long)(implicit session: DBSession = AutoSession): Option[User] =
    toUser(MsUser.find(userId))

  private def findByName(name: String)(implicit session: DBSession = AutoSession): Option[User] = {
    val whereName = sqls" name = ${name} "
    toUser(MsUser.findBy(whereName))
  }

  def findOrCreateByName(name:String)(implicit session: DBSession = AutoSession): User =
    findByName(name).getOrElse(createByName(name))


  def createByName(name:String)(implicit session: DBSession = AutoSession): User = {

    val msUser = MsUser.create(name,"","",DateTime.now(), null)

    new User(msUser.id,msUser.name,msUser.loginid,msUser.password,
             msUser.createdTimestamp.toLocalDateTime,null)

  }

  private def toUser(pMsUser: Option[MsUser]):Option[User] = {
    pMsUser match {
      case Some(m) => {
        Some(new User(m.id, m.name, m.loginid, m.password
          , m.createdTimestamp.toLocalDateTime
          , Some(m.deletedTimestamp.getOrElse(defDateTime).toLocalDateTime)))
      }
      case None => None
    }
  }


}
