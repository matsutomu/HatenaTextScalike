package simple.bookmark.controller

import simple.bookmark.repository.MsUserDefault
import skinny._
import skinny.validator._

import _root_.simple.bookmark.controller._

class MsUsersController extends SkinnyResource with ApplicationController {
  protectFromForgery()

  override def model = MsUserDefault
  override def resourcesName = "msusers"
  override def resourceName = "msuser"

  override def resourcesBasePath = s"/${toSnakeCase(resourcesName)}"
  override def useSnakeCasedParamKeys = true

  override def viewsDirectoryPath = s"/${resourcesName}"

  override def createParams = Params(params).withDateTime("created_timestamp").withDateTime("deleted_timestamp")
  override def createForm = validation(createParams,
    paramKey("name") is required & maxLength(100),
    paramKey("created_timestamp") is required & dateTimeFormat,
    paramKey("deleted_timestamp") is dateTimeFormat
  )
  override def createFormStrongParameters = Seq(
    "name" -> ParamType.String,
    "created_timestamp" -> ParamType.DateTime,
    "deleted_timestamp" -> ParamType.DateTime
  )

  override def updateParams = Params(params).withDateTime("created_timestamp").withDateTime("deleted_timestamp")
  override def updateForm = validation(updateParams,
    paramKey("name") is required & maxLength(100),
    paramKey("created_timestamp") is required & dateTimeFormat,
    paramKey("deleted_timestamp") is dateTimeFormat
  )
  override def updateFormStrongParameters = Seq(
    "name" -> ParamType.String,
    "created_timestamp" -> ParamType.DateTime,
    "deleted_timestamp" -> ParamType.DateTime
  )





}
