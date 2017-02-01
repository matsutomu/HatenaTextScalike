package simple.bookmark.controller

import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import skinny._
import skinny.filter._
import simple.bookmark.model.User
import skinny.micro.context.SkinnyContext
/**
 * The base controller for this Skinny application.
 *
 * see also "http://skinny-framework.org/documentation/controller-and-routes.html"
 */
trait ApplicationController extends SkinnyController
    // with TxPerRequestFilter
    // with SkinnySessionFilter
    with ErrorPageFilter {

  // override def defaultLocale = Some(new java.util.Locale("ja"))

  override def defaultLocale = Some(new java.util.Locale("ja"))


  def saveAuthorizedUser(user: User) = {
    session += "LoginUserId" -> user
  }

  def getCurrentUserId:Option[User] = session.getAs[User]("LoginUserId")


  protected def Unauthorized(ctx:SkinnyContext) = {
    redirect("/")(ctx)
  }

  protected def getPermParaAsString(name:String, permParam: PermittedStrongParameters):String = {
    permParam.params(name)._1.toString()
  }

}
