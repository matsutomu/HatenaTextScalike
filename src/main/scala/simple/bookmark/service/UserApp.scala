package simple.bookmark.service

import scalikejdbc.{AutoSession, DBSession}
import simple.bookmark.model.User
import simple.bookmark.repository.Users



object UserApp {

  def authentication(loginId: String, password:String)(implicit session: DBSession = AutoSession): Option[User] = {
    Users.findByLoginId(loginId).collect{
      // case user if(user.password == "") => Some(user)
      case user if(true) => Some(user)
    }getOrElse(None)
  }


}

