package simple.bookmark.controller

import skinny._
import skinny.validator._
import java.util.Locale

import org.joda.time.LocalDateTime
import skinny.filter._
import simple.bookmark.service.BookmarkApp
import simple.bookmark.model.Bookmark

/**
  *
  */
class BookmarksController extends ApplicationController {
  protectFromForgery()

  val row_cnt:Int = 5
  //before(){layout("default_bookmarks.ssp")}

//  beforeAction(only = Seq('index, 'indexWithSlash, 'new, 'create, 'createWithSlash, 'edit, 'update)) {
//    set("companies", Company.findAll())
//    set("skills", Skill.findAll())
//  }

  def index = {
    val booApp: BookmarkApp = new BookmarkApp("matsutomu")
    set("page_max",  getPageMax(booApp))
    set("bookmarks", booApp.listPaged(0,row_cnt))
    render(s"/bookmarks/index")
  }

  def pageGet = params.getAs[Int]("page").map { page =>
    val booApp: BookmarkApp = new BookmarkApp("matsutomu")

    val pageMax = getPageMax(booApp)
    if(page <= pageMax){
      set("page_max",  pageMax)
      set("bookmarks", booApp.listPaged(((page - 1)*row_cnt)  , row_cnt))
      render(s"/bookmarks/index")
    }
    else
      haltWithBody(404)
  }.getOrElse(
    haltWithBody(404)
  )

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

  def show = params.getAs[Long]("id").map { id =>
    val booApp: BookmarkApp = new BookmarkApp("matsutomu")

    booApp.find(id).map{ item =>
      set("bookmark", item)
      render("/bookmarks/show")
    }.getOrElse( haltWithBody(404))
  }.getOrElse( haltWithBody(404))


  def editGet = params.getAs[Long]("id").map { id =>
    val booApp: BookmarkApp = new BookmarkApp("matsutomu")

    booApp.find(id).map { item =>
      set("bookmark", item)
      render(s"/bookmarks/edit")
    }.getOrElse(haltWithBody(404))

  }.getOrElse(
    haltWithBody(404)
  )

  def editPost = params.getAs[Long]("id").map { id =>
    val booApp: BookmarkApp = new BookmarkApp("matsutomu")
    if(updateForm.validate()){
      val parameters = updateParams.permit(updateFormStrongParameters:_*)

      booApp.find(id).map { item =>
        booApp.edit(id, parameters.params("comment")._1.toString() )

        //set("bookmarks",booApp.list())
        redirect("/bookmarks")
      }.getOrElse(haltWithBody(404))

    } else {
      booApp.find(id).map { item =>
        set("bookmark", item)
        render("/bookmarks/edit")
      }.getOrElse(haltWithBody(404))
    }
  }.getOrElse( haltWithBody(404))


  def delete = params.getAs[Long]("id").map { id =>
    val booApp: BookmarkApp = new BookmarkApp("matsutomu")

    booApp.find(id).map { item =>
      booApp.delete(id)
      set("page_max",  getPageMax(booApp))
      set("bookmarks", booApp.listPaged(0,row_cnt))
      render(s"/bookmarks")
    }.getOrElse(haltWithBody(404))

  }.getOrElse( haltWithBody(404))


  def addGet = {
    val defaultTime = LocalDateTime.now()
    set("bookmark", new simple.bookmark.model.Bookmark(0, simple.bookmark.model.User(0, "",defaultTime, Some(defaultTime)),
                                                          simple.bookmark.model.Entry(0, "", Some(""),defaultTime,Some(defaultTime))
                                                       ,"", defaultTime,defaultTime))
    render("/bookmarks/new")
  }

  def addPost = {
    val booApp: BookmarkApp = new BookmarkApp("matsutomu")

    if(createForm.validate()){
      val parameters = createParams.permit(createFormStrongParameters:_*)
      booApp.add(parameters.params("url")._1.toString, parameters.params("comment")._1.toString)

      set("page_max",  getPageMax(booApp))
      set("bookmarks", booApp.listPaged(0,row_cnt))

      render(s"/bookmarks/index")

    } else {
      val defaultTime = LocalDateTime.now()
      set("bookmark", new simple.bookmark.model.Bookmark(0, simple.bookmark.model.User(0, "",defaultTime, Some(defaultTime)),
        simple.bookmark.model.Entry(0, "", Some(""),defaultTime,Some(defaultTime))
        ,"", defaultTime,defaultTime))

      render("/bookmarks/new")

    }

  }

  def getPageMax(booApp: BookmarkApp) =  Math.ceil(booApp.count().toDouble / row_cnt).toLong


}
