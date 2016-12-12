package simple.bookmark.controller

import simple.bookmark.controller.Controllers._
import skinny._
import skinny.controller.AssetsController

object Controllers {

  def mount(ctx: ServletContext): Unit = {
    // error controller ?

    root.mount(ctx)
    bookmarks.mount(ctx)
    msusers.mount(ctx)
    AssetsController.mount(ctx)

  }

  object root extends RootController with Routes {

    // layout change is OK ?
    before(){
      //set("current","user")
      set("current","user")
      layout("default.ssp")
    }

    val indexUrl = get("/?")(index).as('index)
  }

  object bookmarks extends BookmarksController with Routes {

    before(){
      //set("current","user")
      set("current","bookmarks")
      layout("default_bookmarks.ssp")
    }

    val indexUrl = get("/bookmarks")(index).as('index)

    val pageUrl = get("/bookmarks/:page/page")(pageGet).as('pageget)

    val showUrl = get("/bookmarks/:id")(show).as('show)

    val editGetUrl = get("/bookmarks/:id/edit")(editGet).as('editget)

    val editPostUrl = post("/bookmarks/:id/edit")(editPost).as('editpost)

    val destroyUrl = delete("/bookmarks/:id")(delete).as('delete)

    val addGetUrl = get("/bookmarks/new")(addGet).as('addget)

    val addPostUrl = post("/bookmarks/new")(addPost).as('addpost)
  }

  object msusers extends _root_.simple.bookmark.controller.MsUsersController with Routes {

    before(){
      //set("current","user")
      set("current","user")
      layout("default_bookmarks.ssp")
    }

  }



}

