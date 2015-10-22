import org.specs2.mutable._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._


class ItemRestAPISpec extends Specification {

  "Application" should {

    "render Cart REST service" in new WithApplication {

      // Insert
      val data = Json.obj(
        "id" -> 101,
        "name" -> "Test 101",
        "price" -> 101.10,
        "stock" -> 101
      )
      val postRequest = FakeRequest(
        method = "POST",
        uri = "/item",
        headers = FakeHeaders(
          Seq("Content-type" -> Seq("application/json"))
        ),
        body = data
      )
      val Some(result) = route(postRequest)
      status(result) must equalTo(OK)

      // Get Inserted Data
      val getDataInserted = route(FakeRequest(GET, "/item")).get
      status(getDataInserted) must equalTo(OK)
      contentType(getDataInserted) must beSome.which(_ == "application/json")
      contentAsString(getDataInserted) must contain("Test 101")

      // Update Data
      val dataUpdate = Json.obj(
        "id" -> 101,
        "name" -> "Test 101 Updated",
        "price" -> 101.10,
        "stock" -> 101
      )
      val postUpdate = FakeRequest(
        method = "PUT",
        uri = "/item/101",
        headers = FakeHeaders(
          Seq("Content-type" -> Seq("application/json"))
        ),
        body = dataUpdate
      )
      val Some(getUpdate) = route(postUpdate)
      status(getUpdate) must equalTo(OK)

      // Get Updated Data
      val getDataUpdate = route(FakeRequest(GET, "/item")).get
      status(getDataUpdate) must equalTo(OK)
      contentType(getDataUpdate) must beSome.which(_ == "application/json")
      contentAsString(getDataUpdate) must contain("Test 101 Updated")

      // Delete Data
      val deleteData = route(FakeRequest(DELETE, "/item/101")).get
      status(deleteData) must equalTo(OK)

    }
  }
}