package simple.bookmark.repository

import org.joda.time._
import scalikejdbc._
import skinny.orm._

case class MsUser(
  id: Long,
  name: String,
  createdTimestamp: DateTime,
  deletedTimestamp: Option[DateTime] = None
)

object MsUser extends SkinnyCRUDMapper[MsUser] {
  override lazy val tableName = "ms_user"
  override lazy val defaultAlias = createAlias("mu")

  /*
   * If you're familiar with ScalikeJDBC/Skinny ORM, using #autoConstruct makes your mapper simpler.
   * (e.g.)
   * override def extract(rs: WrappedResultSet, rn: ResultName[MsUser]) = autoConstruct(rs, rn)
   *
   * Be aware of excluding associations like this:
   * (e.g.)
   * case class Member(id: Long, companyId: Long, company: Option[Company] = None)
   * object Member extends SkinnyCRUDMapper[Member] {
   *   override def extract(rs: WrappedResultSet, rn: ResultName[Member]) =
   *     autoConstruct(rs, rn, "company") // "company" will be skipped
   * }
   */
  override def extract(rs: WrappedResultSet, rn: ResultName[MsUser]): MsUser = new MsUser(
    id = rs.get(rn.id),
    name = rs.get(rn.name),
    createdTimestamp = rs.get(rn.createdTimestamp),
    deletedTimestamp = rs.get(rn.deletedTimestamp)
  )
}
