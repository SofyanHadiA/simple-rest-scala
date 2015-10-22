package controllers

import models._
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.mvc._
import tables._

object CartItemController extends Controller {

  val cartItems = TableQuery[CartItemsTable]

  //JSON read/write macro
  implicit val cartItemFormat = Json.format[CartItem]

  def list(cartId: Int) = DBAction { implicit rs =>
    val q = cartItems.filter(_.cartId === cartId)
    Ok(toJson(q.list))
  }

  def retrieve(cartId: Int, itemId: Int) = DBAction { implicit rs =>
    val q = cartItems.filter(_.cartId === cartId).filter(_.itemId === itemId).firstOption
    Ok(toJson(q))
  }

  def insert(cartId: Int) = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[CartItem].map { cartItem =>
      cartItems.insert(cartItem)
      Ok(toJson(cartItem))
    }.getOrElse(BadRequest("invalid json"))
  }

  def update(cartId: Int, itemId: Int) = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[CartItem].map { cartItem =>
      val q = cartItems.filter(_.cartId === cartId).filter(_.itemId === itemId)
      if (q.exists.run) {
        cartItems.update(cartItem)
        Ok(toJson(cartItem))
      }
      else {
        BadRequest(Json.obj("status" -> "KO", "message" -> "cart item not found"))
      }
    }.getOrElse(BadRequest("invalid json"))
  }

  def delete(cartId: Int, itemId: Int) = DBAction { implicit rs =>
    val q = cartItems.filter(_.cartId === cartId).filter(_.itemId === itemId)
    if (q.exists.run) {
      q.delete
      Ok(Json.obj("status" -> "OK", "message" -> "cart item was deleted"))
    }
    else {
      BadRequest(Json.obj("status" -> "KO", "message" -> "cart item not found"))
    }
  }
}
