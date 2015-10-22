package test

import play.api.http.Writeable
import play.api.test.{FakeHeaders, FakeRequest, PlaySpecification, WithApplication}

class ItemRestSpec extends PlaySpecification {

  "Item Controller " should {

    "insert item bad request" in new WithApplication {
      val item = route(FakeRequest(POST, "/item")).get

      status(item) mustEqual BAD_REQUEST
    }

    "insert item success" in new WithApplication {
      val result = route(FakeRequest(POST, "/item",
        FakeHeaders(Seq(CONTENT_TYPE -> Seq("application/json"))),
        """{"id":101, "name":"test 101", "price":100, "stock":100}"""))(Writeable(_.getBytes, None)).get
      status(result) mustEqual OK
    }

    "render item list" in new WithApplication {
      val item = route(FakeRequest(GET, "/item")).get

      status(item) mustEqual OK
      contentType(item) must beSome.which(_ == "application/json")
    }

    "update item bad request" in new WithApplication {
      route(FakeRequest(POST, "/item",
        FakeHeaders(Seq(CONTENT_TYPE -> Seq("application/json"))),
        """{"id":101, "name":"test 101", "price":100, "stock":100}"""))(Writeable(_.getBytes, None)).get

      val item = route(FakeRequest(PUT, "/item/101")).get
      status(item) mustEqual BAD_REQUEST
    }

    "update item success" in new WithApplication {
      val result = route(FakeRequest(PUT, "/item/101",
        FakeHeaders(Seq(CONTENT_TYPE -> Seq("application/json"))),
        """{"id":101, "name":"Test 101 updated", "price":200, "stock":200}"""))(Writeable(_.getBytes, None)).get
      status(result) mustEqual OK
    }

    "delete item success" in new WithApplication {
      val result = route(FakeRequest(DELETE, "/item/101")).get
      status(result) mustEqual OK
    }
  }
}