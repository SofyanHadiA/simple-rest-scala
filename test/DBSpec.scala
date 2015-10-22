package test

import org.specs2.mutable._

import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import play.api.test._
import play.api.test.Helpers._
import models._
import tables._

/**
 * test the item table database
 */
class DBSpec extends Specification {

  "DB" should {
    "work as expected" in new WithApplication {

      //create an instance of the table
      val Items = TableQuery[ItemsTable]
      val Carts = TableQuery[CartsTable]
      val CartItems = TableQuery[CartItemsTable]

      DB.withSession { implicit s: Session =>
        // test Items Table
        val testItems = Seq(
          Item(101, "Item 101", 101, 101),
          Item(102, "Item 102", 102, 102),
          Item(103, "Item 103", 103, 103))

        Items.insertAll(testItems: _*)
        Items.list must equalTo(testItems)

        // test Carts Table
        val testCarts = Seq(
          Cart(201, 201201),
          Cart(202, 202202),
          Cart(203, 203203))

        Carts.insertAll(testCarts: _*)
        Carts.list must equalTo(testCarts)

        // test Cart Items Table
        val testCartItems = Seq(
          CartItem(201, 101, 10),
          CartItem(202, 102, 20),
          CartItem(203, 103, 30))

        CartItems.insertAll(testCartItems: _*)
        CartItems.list must equalTo(testCartItems)
      }
    }

    "select the correct testing db settings by default" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      DB.withSession { implicit s: Session =>
        s.conn.getMetaData.getURL must startWith("jdbc:h2:mem:play-test")
      }
    }

    "use the default db settings when no other possible options are available" in new WithApplication {
      DB.withSession { implicit s: Session =>
        s.conn.getMetaData.getURL must equalTo("jdbc:h2:mem:play")
      }
    }
  }

}
