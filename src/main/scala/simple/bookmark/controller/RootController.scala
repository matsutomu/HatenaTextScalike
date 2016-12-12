package simple.bookmark.controller

import skinny._

class RootController extends  ApplicationController {



  def index = {
    layout("default.ssp")
    render("/root/index")
  }

  def signin = {
    layout("default.ssp")
    render("/root/index")
  }

  def signout = {
    layout("default.ssp")
    render("/root/index")
  }

}
