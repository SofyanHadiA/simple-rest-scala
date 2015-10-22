package tables

import models.{Cart, CartItem, Item}
import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted.ForeignKeyQuery

class CartItemsTable(tag: Tag) extends Table[CartItem](tag, "CART_ITEMS") {

  def cartId = column[Int]("CART_ID", O.PrimaryKey)

  def itemId = column[Int]("ITEM_ID")

  def quantity = column[Int]("QUANTITY")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (cartId, itemId, quantity) <>(CartItem.tupled, CartItem.unapply _)

  // A reified foreign key relation that can be navigated to create a join
  def cart: ForeignKeyQuery[CartsTable, Cart] =
    foreignKey("CART_FK", cartId, TableQuery[CartsTable])(_.id)

  def item: ForeignKeyQuery[ItemsTable, Item] =
    foreignKey("ITEM_FK", itemId, TableQuery[ItemsTable])(_.id)

}

