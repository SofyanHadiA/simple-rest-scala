package test

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import slick.driver.H2Driver.api._
import slick.jdbc.meta._

import models.{Cart, Item}
import tables.{CartItemsTable, CartsTable, ItemsTable, CartItem}

class TablesSuite extends FunSuite with BeforeAndAfter with ScalaFutures {
  implicit override val patienceConfig = PatienceConfig(timeout = Span(5, Seconds))

  val item = TableQuery[ItemsTable]
  val cart = TableQuery[CartsTable]
  val cartItem = TableQuery[CartItemsTable]

  var db: Database = _

  def createSchema() = db.run((item.schema ++ cart.schema ++ cartItem.schema).create).futureValue

  def insertItem(): Int =
    db.run(item += Item(101, "Test Item", 1000.00, 100)).futureValue

  def insertEmptyCart(): Int =
    db.run(cart += Cart(201, 0.00)).futureValue

  def insertCartItem(): Int =
    db.run(cartItem += CartItem(201, 101, 5)).futureValue

  before {
    db = Database.forConfig("default")
  }

  test("Creating the Schema works") {
    createSchema()

    val tables = db.run(MTable.getTables).futureValue

    assert(tables.size == 3)
    assert(tables.count(_.name.name.equalsIgnoreCase("items")) == 1)
    assert(tables.count(_.name.name.equalsIgnoreCase("carts")) == 1)
    assert(tables.count(_.name.name.equalsIgnoreCase("cart_items")) == 1)
  }

  test("Inserting an Item works") {
    createSchema()

    val insertCount = insertItem()
    assert(insertCount == 1)
  }

  test("Query Items works") {
    createSchema()
    insertItem()
    val results = db.run(item.result).futureValue
    assert(results.size == 1)
    assert(results.head.id == 101)
  }

  test("Create an Empty Cart works") {
    createSchema()

    val insertCount = insertEmptyCart()
    assert(insertCount == 1)
  }

  test("Query an Empty Cart works") {
    createSchema()
    insertEmptyCart()
    val results = db.run(cart.result).futureValue
    assert(results.size == 1)
    assert(results.head.id == 201)
  }

  test("Add an Item to Cart works") {
    createSchema()

    insertItem()
    insertEmptyCart()

    val insertCount = insertCartItem()
    assert(insertCount == 1)
  }

  test("Query Cart Item works") {
    createSchema()

    insertItem()
    insertEmptyCart()

    insertCartItem()
    val results = db.run(cart.result).futureValue
    assert(results.size == 1)
  }

  after {
    db.close
  }
}
