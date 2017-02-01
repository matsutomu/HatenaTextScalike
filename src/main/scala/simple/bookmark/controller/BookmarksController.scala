package simple.bookmark.controller

import skinny._
import skinny.validator._
import java.util.Locale

import org.joda.time.LocalDateTime
import simple.bookmark.controller.Controllers.root._
import skinny.filter._
import simple.bookmark.service.BookmarkApp
import simple.bookmark.util.LoginAuthenticate
import simple.bookmark.model.User

/**
  *
  */
class BookmarksController extends ApplicationController with LoginAuthenticate {
  protectFromForgery()

  val row_cnt:Int = 5

//  beforeAction(only = Seq('index, 'indexWithSlash, 'new, 'create, 'createWithSlash, 'edit, 'update)) {
//    set("companies", Company.findAll())
//    set("skills", Skill.findAll())
//  }



  /*
   * index
   */
  def index = loginOnly(getCurrentUserId) { usr =>
      val booApp: BookmarkApp = new BookmarkApp(usr.name)
      set("page_max", getPageMax(booApp))
      set("current_page" , 1l)
      set("bookmarks", booApp.listPaged(0, row_cnt))
      render(s"/bookmarks/index")
  }


  /*
   * page
   */
  def pageGet = loginOnly(getCurrentUserId) { usr =>
    val booApp: BookmarkApp = new BookmarkApp(usr.name)
    val pageMax = getPageMax(booApp)
    (
      for {
        page <- params.getAs[Int]("page") if page <= pageMax
      } yield {
        set("page_max",  pageMax)
        set("current_page" , page.toLong )
        set("bookmarks", booApp.listPaged(((page - 1) * row_cnt), row_cnt))
        render(s"/bookmarks/index")
      }
      ) getOrElse {
      haltWithBody(404)
    }
  }

  /*
   * show detail
   */
  def show = loginOnly(getCurrentUserId) { usr =>
    val booApp: BookmarkApp = new BookmarkApp(usr.name)
    (
    for{
        id <- params.getAs[Long]("id")
        item <- booApp.find(id)
      }yield {
        set("bookmark", item)
        render("/bookmarks/show")
    } ) getOrElse {
      haltWithBody(404)
    }
  }


  def createParams = Params(params)
  def createForm = validation(createParams,
    paramKey("url") is required & maxLength(255),
    paramKey("comment") is required & maxLength(100)
  )
  def createFormStrongParameters = Seq(
    "url" -> ParamType.String,
    "comment" -> ParamType.String
  )

  def updateParams = Params(params)
  def updateForm = validation(createParams,
    paramKey("comment") is required & maxLength(100)
  )
  def updateFormStrongParameters = Seq(
    "comment" -> ParamType.String
  )


  /*
   * edit site
   */
  def editGet = loginOnly(getCurrentUserId) { usr =>
    val booApp: BookmarkApp = new BookmarkApp(usr.name)
    (for{
      id   <- params.getAs[Long]("id")
      item <- booApp.find(id)
    } yield {
      set("bookmark", item)
      render(s"/bookmarks/edit")
    }) getOrElse {
        haltWithBody(404)
    }
  }

  /*
   * edit
   */
  def editPost = loginOnly(getCurrentUserId) { usr =>
    val booApp: BookmarkApp = new BookmarkApp(usr.name)

    (for {
      id   <- params.getAs[Long]("id")
      item <- booApp.find(id)
    } yield {
      if (updateForm.validate()) {
        val parameters = updateParams.permit(updateFormStrongParameters: _*)
        booApp.edit(id, getPermParaAsString("comment",parameters))
        redirect("/bookmarks")
      } else {
        set("bookmark", item)
        render("/bookmarks/edit")
      }
    }) getOrElse {
      haltWithBody(404)
    }
  }

  /*
   * delete
   */
  def delete = loginOnly(getCurrentUserId) { usr =>
    val booApp: BookmarkApp = new BookmarkApp(usr.name)

    (for {
      id   <- params.getAs[Long]("id")
      item <- booApp.find(id)
      } yield {
        booApp.delete(id)
        set("page_max", getPageMax(booApp))
        set("current_page" , 1l)
        set("bookmarks", booApp.listPaged(0, row_cnt))
        render(s"/bookmarks")
      }
    ).getOrElse(haltWithBody(404))
  }

  /*
   * add / default
   */
  def addGet = loginOnly(getCurrentUserId) { usr =>
    set("bookmark", BookmarkApp.createDefault())
    render("/bookmarks/new")
  }

  /*
   * add / post
   */
  def addPost = loginOnly(getCurrentUserId) { usr =>
      val booApp: BookmarkApp = new BookmarkApp(usr.name)

      if (createForm.validate()) {
        val parameters = createParams.permit(createFormStrongParameters: _*)
        val entry  = booApp.findEntryByUrl(getPermParaAsString("url",parameters))
        val result = booApp.add(getPermParaAsString("url",parameters),
                                getPermParaAsString("comment",parameters))
        result match {
          case Right(res) => {
            set("page_max", getPageMax(booApp))
            set("current_page" , 1l)
            set("bookmarks", booApp.listPaged(0, row_cnt))

            entry match {
              case Some(e) => flash += ("msg" -> createI18n().getOrKey("bookmarks.flash.updated"))
              case None    => flash += ("msg" -> createI18n().getOrKey("bookmarks.flash.created"))
            }

            render(s"/bookmarks/index")
          }
          case Left(err) => {
            //todo err message output
            set("bookmark", BookmarkApp.createDefault())
            render("/bookmarks/new")
          }
        }

      } else {
        set("bookmark", BookmarkApp.createDefault())
        render("/bookmarks/new")

      }
  }

  private def getPageMax(booApp: BookmarkApp) =  Math.ceil(booApp.count().toDouble / row_cnt).toLong


}
