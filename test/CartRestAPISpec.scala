package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json


class CartRestAPISpec extends Specification {

  "Application" should {

    "render Cart REST service" in new WithApplication {

      // Insert
      val data = Json.obj(
        "id" -> 201,
        "total" -> 201201
      )
      val postRequest = FakeRequest(
        method = "POST",
        uri = "/cart",
        headers = FakeHeaders(
          Seq("Content-type" -> Seq("application/json"))
        ),
        body = data
      )
      val Some(result) = route(postRequest)
      status(result) must equalTo(OK)

      // Get Inserted Data
      val getDataInserted = route(FakeRequest(GET, "/cart")).get
      status(getDataInserted) must equalTo(OK)
      contentType(getDataInserted) must beSome.which(_ == "application/json")
      contentAsString(getDataInserted) must contain("201")

      // Update Data
      val dataUpdate = Json.obj(
        "id" -> 201,
        "total" -> 20000
      )
      val postUpdate = FakeRequest(
        method = "PUT",
        uri = "/cart/201",
        headers = FakeHeaders(
          Seq("Content-type" -> Seq("application/json"))
        ),
        body = dataUpdate
      )
      val Some(getUpdate) = route(postUpdate)
      status(getUpdate) must equalTo(OK)

      // Get Updated Data
      val getDataUpdate = route(FakeRequest(GET, "/cart")).get
      status(getDataUpdate) must equalTo(OK)
      contentType(getDataUpdate) must beSome.which(_ == "application/json")
      contentAsString(getDataUpdate) must contain("20000")

      // Delete Data
      val deleteData = route(FakeRequest(DELETE, "/cart/201")).get
      status(deleteData) must equalTo(OK)

    }
  }
}