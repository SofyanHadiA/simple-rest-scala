package controllers

import models._
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.mvc._
import tables._

object ItemController extends Controller {

  val items = TableQuery[CartsTable]

  //JSON read/write macro
  implicit val cartFormat = Json.format[Cart]

  def list = DBAction { implicit rs =>
    Ok(toJson(items.list))
  }

  def retrieve(id: Int) = DBAction { implicit rs =>
    val q = items.filter(_.id === id).firstOption
    Ok(toJson(q))
  }

  def insert = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[Item].map { item =>
      items.insert(item)
      Ok(toJson(item))
    }.getOrElse(BadRequest("invalid json"))
  }

  def update(id: Int) = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[Item].map { item =>
      val q = items.filter(_.id === id)
      if (q.exists.run) {
        items.update(item)
        Ok(toJson(item))
      }
      else {
        BadRequest(Json.obj("status" -> "KO", "message" -> "cart not found"))
      }
    }.getOrElse(BadRequest("invalid json"))
  }

  def delete(id: Int) = DBAction { implicit rs =>
    val q = items.filter(_.id === id)
    if (q.exists.run) {
      q.delete
      Ok(Json.obj("status" -> "OK", "message" -> "cart was deleted"))
    }
    else {
      BadRequest(Json.obj("status" -> "KO", "message" -> "cart not found"))
    }
  }
}
