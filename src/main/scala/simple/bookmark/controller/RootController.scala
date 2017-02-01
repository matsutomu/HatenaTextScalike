package simple.bookmark.controller

import simple.bookmark.model.User
import skinny._
import skinny.validator.{maxLength, required, _}
import simple.bookmark.service.UserApp
import skinny.PermittedStrongParameters

class RootController extends  ApplicationController {

  /*
   * index
   */
  def index = {
    render("/root/index")
  }


  /*
   * login
   */
  def loginParams = Params(params)

  def loginForm = validation(loginParams,
    paramKey("loginid")  is required & maxLength(10),
    paramKey("password") is required & maxLength(10)
  )

  def loginFormStrongParameters = Seq(
    "loginid"  -> ParamType.String,
    "password" -> ParamType.String
  )


  def login = {
    if(loginForm.validate()) {
      val parameters = loginParams.permit(loginFormStrongParameters:_*)

      UserApp.authentication(getPermParaAsString("loginid",parameters)
                            ,getPermParaAsString("password",parameters)) match
      {
        case Some(usr) => loginRedirect(usr)
        case _ => {
          flash += ("warn" -> createI18n().getOrKey("login.authentication.failmessage"))
          redirect("/")
        }
      }
    }else{
      status = 400
      render("/root/index")
    }
  }

  private def loginRedirect(usr: User) = {
    saveAuthorizedUser(usr)
    redirect("/bookmarks")
  }

  /*
   *
   */
  def logout = {
    //todo session clear

    render("/root/index")
  }

}
