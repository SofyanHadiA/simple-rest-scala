package controllers

import models._
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.mvc._
import tables._

object CartController extends Controller {

  val carts = TableQuery[CartsTable]

  //JSON read/write macro
  implicit val cartFormat = Json.format[Cart]

  def list = DBAction { implicit rs =>
    Ok(toJson(carts.list))
  }

  def retrieve(id: Int) = DBAction { implicit rs =>
    val q = carts.filter(_.id === id).firstOption
    Ok(toJson(q))
  }

  def insert = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[Cart].map { cart =>
      carts.insert(cart)
      Ok(toJson(cart))
    }.getOrElse(BadRequest("invalid json"))
  }

  def update(id: Int) = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[Cart].map { cart =>
      val q = carts.filter(_.id === id)
      if (q.exists.run) {
        carts.update(cart)
        Ok(toJson(cart))
      }
      else {
        BadRequest(Json.obj("status" -> "KO", "message" -> "cart not found"))
      }
    }.getOrElse(BadRequest("invalid json"))
  }

  def delete(id: Int) = DBAction { implicit rs =>
    val q = carts.filter(_.id === id)
    if (q.exists.run) {
      q.delete
      Ok(Json.obj("status" -> "OK", "message" -> "cart was deleted"))
    }
    else {
      BadRequest(Json.obj("status" -> "KO", "message" -> "cart not found"))
    }
  }
}
