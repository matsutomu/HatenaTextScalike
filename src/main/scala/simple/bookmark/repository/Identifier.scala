package simple.bookmark.repository
import scalikejdbc._
import org.joda.time.{DateTime}

/**
  * Created by matsutomu on 16/02/10.
  */
object Identifier {
  def generate(implicit session: DBSession = AutoSession): Option[String] =
    sql"SELECT UUID() as ID".map{rs => rs.nString("ID")}.single.apply()
}
