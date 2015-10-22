package tables

import models.Cart
import play.api.db.slick.Config.driver.simple._

// A Carts table with 2 columns: id, total
class CartsTable(tag: Tag) extends Table[Cart](tag, "CARTS") {

  def id = column[Int]("CART_ID", O.PrimaryKey)

  def total = column[Double]("TOTAL")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, total) <>(Cart.tupled, Cart.unapply _)
}


