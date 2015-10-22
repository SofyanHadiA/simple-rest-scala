import org.specs2.mutable._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._


class CartItemRestAPISpec extends Specification {

  "Application" should {

    "render Cart Item REST service" in new WithApplication {

      // Insert Item
      val item = Json.obj(
        "id" -> 101,
        "name" -> "Test 101",
        "price" -> 101.10,
        "stock" -> 101
      )
      val postItem = FakeRequest(
        method = "POST",
        uri = "/item",
        headers = FakeHeaders(
          Seq("Content-type" -> Seq("application/json"))
        ),
        body = item
      )
      val Some(resultItem) = route(postItem)
      status(resultItem) must equalTo(OK)

      // insert Cart
      val cart = Json.obj(
        "id" -> 201,
        "total" -> 201201
      )
      val postCart = FakeRequest(
        method = "POST",
        uri = "/cart",
        headers = FakeHeaders(
          Seq("Content-type" -> Seq("application/json"))
        ),
        body = cart
      )
      val Some(resultCart) = route(postCart)
      status(resultCart) must equalTo(OK)

      // Insert
      val data = Json.obj(
        "cartId" -> 201,
        "itemId" -> 101,
        "quantity" -> 1000
      )
      val postRequest = FakeRequest(
        method = "POST",
        uri = "/cart/201/addItem",
        headers = FakeHeaders(
          Seq("Content-type" -> Seq("application/json"))
        ),
        body = data
      )
      val Some(result) = route(postRequest)
      status(result) must equalTo(OK)

      // Get Inserted Data  /cart/:cartId/items
      val getDataInserted = route(FakeRequest(GET, "/cart/201/items")).get
      status(getDataInserted) must equalTo(OK)
      contentType(getDataInserted) must beSome.which(_ == "application/json")
      contentAsString(getDataInserted) must contain("201")

      // Update Data
      val dataUpdate = Json.obj(
        "cartId" -> 201,
        "itemId" -> 101,
        "quantity" -> 5000
      )
      val postUpdate = FakeRequest(
        method = "PUT",
        uri = "/cart/201/updateitem/101", ///cart/:cartId/updateItem/:itemId
        headers = FakeHeaders(
          Seq("Content-type" -> Seq("application/json"))
        ),
        body = dataUpdate
      )
      val Some(getUpdate) = route(postUpdate)
      status(getUpdate) must equalTo(OK)

      // Get Updated Data
      val getDataUpdate = route(FakeRequest(GET, "/cart/201/items")).get
      status(getDataUpdate) must equalTo(OK)
      contentType(getDataUpdate) must beSome.which(_ == "application/json")
      contentAsString(getDataUpdate) must contain("5000")

      // Delete Data /cart/:cartId/deleteItem/:itemId
      val deleteData = route(FakeRequest(DELETE, "/cart/201/deleteitem/101")).get
      status(deleteData) must equalTo(OK)

    }
  }
}