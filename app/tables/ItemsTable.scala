package tables

import models.Item
import play.api.db.slick.Config.driver.simple._

// An Item table with 4 columns: id, name, price, stock
class ItemsTable(tag: Tag) extends Table[Item](tag, "ITEMS") {

  // This is the primary key column:
  def id = column[Int]("ITEM_ID", O.PrimaryKey)

  def name  = column[String]("NAME")

  def price = column[Double]("PRICE")

  def stock = column[Int]("STOCK")

  def * = (id, name, price, stock) <>(Item.tupled, Item.unapply _)
}