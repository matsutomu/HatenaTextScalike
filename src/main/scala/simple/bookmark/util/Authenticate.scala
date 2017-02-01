package simple.bookmark.util

import javax.servlet.http.HttpServletResponse

import simple.bookmark.controller.ApplicationController
import simple.bookmark.model.User
import skinny.micro.context.SkinnyContext
/*
*
* refer)
* https://github.com/gitbucket/gitbucket/blob/6c6126148eab78aaea01896cedc746d46476d54d/src/main/scala/gitbucket/core/util/Authenticator.scala
*
*/

trait LoginAuthenticate { self: ApplicationController =>

  protected def loginOnly(usr:Option[User])(action: (User) => Any) = { authenticate(usr)(action) }

  private def authenticate(usr:Option[User])(action: (User) => Any) = {
      usr.map { u =>
        action(u)
      }getOrElse{
        Unauthorized(context)
      }


  }
}
