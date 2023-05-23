/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package api.connectors

import api.connectors.DownstreamUri.{DesUri, IfsUri, TaxYearSpecificIfsUri}
import api.models.outcomes.ResponseWrapper
import config.AppConfig
import mocks.{MockAppConfig, MockHttpClient}
import uk.gov.hmrc.http.{HttpClient, HttpReads}

import scala.concurrent.Future

class BaseDownstreamConnectorSpec extends ConnectorSpec {
  // WLOG
  val body        = "body"
  val outcome     = Right(ResponseWrapper(correlationId, Result(2)))
  val url         = "some/url?param=value"
  val absoluteUrl = s"$baseUrl/$url"
  val qps         = Seq("param1" -> "value1")

  // WLOG
  case class Result(value: Int)

  implicit val httpReads: HttpReads[DownstreamOutcome[Result]] = mock[HttpReads[DownstreamOutcome[Result]]]

  class Test extends MockHttpClient with MockAppConfig {

    val connector: BaseDownstreamConnector = new BaseDownstreamConnector {
      val http: HttpClient     = mockHttpClient
      val appConfig: AppConfig = mockAppConfig
    }

  }

  "for DES" when {
    "post" must {
      "posts with the required des headers and returns the result" in new Test with DesTest {
        val requiredDesHeadersPost: Seq[(String, String)] = requiredDesHeaders ++ Seq("Content-Type" -> "application/json")
        MockHttpClient
          .post(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            body,
            requiredHeaders = requiredDesHeadersPost,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.post(body, DesUri[Result](url))) shouldBe outcome
      }
    }

    "get" must {
      "get with the required des headers and return the result" in new Test with DesTest {
        MockHttpClient
          .get(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            parameters = qps,
            requiredHeaders = requiredDesHeaders,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.get(DesUri[Result](url), queryParams = qps)) shouldBe outcome
      }
    }

    "delete" must {
      "delete with the required des headers and return the result" in new Test with DesTest {
        MockHttpClient
          .delete(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            requiredHeaders = requiredDesHeaders,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.delete(DesUri[Result](url))) shouldBe outcome
      }
    }

    "put" must {
      "put with the required des headers and return result" in new Test with DesTest {
        val requiredDesHeadersPut: Seq[(String, String)] = requiredDesHeaders ++ Seq("Content-Type" -> "application/json")
        MockHttpClient
          .put(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            body,
            requiredHeaders = requiredDesHeadersPut,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.put(body, DesUri[Result](url))) shouldBe outcome
      }
    }

    "content-type header already present and set to be passed through" must {
      "override (not duplicate) the value" when {
        testNoDuplicatedContentType("Content-Type" -> "application/user-type")
        testNoDuplicatedContentType("content-type" -> "application/user-type")

        def testNoDuplicatedContentType(userContentType: (String, String)): Unit =
          s"for user content type header $userContentType" in new Test with DesTest {
            MockHttpClient
              .put(
                absoluteUrl,
                config = dummyHeaderCarrierConfig,
                body,
                requiredHeaders = requiredDesHeaders ++ Seq("Content-Type" -> "application/json"),
                excludedHeaders = Seq(userContentType)
              )
              .returns(Future.successful(outcome))

            await(connector.put(body, DesUri[Result](url))) shouldBe outcome
          }
      }
    }
  }

  "for IFS" when {
    "post" must {
      "posts with the required ifs headers and returns the result" in new Test with IfsTest {
        val requiredIfsHeadersPost: Seq[(String, String)] = requiredIfsHeaders ++ Seq("Content-Type" -> "application/json")
        MockHttpClient
          .post(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            body,
            requiredHeaders = requiredIfsHeadersPost,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.post(body, IfsUri[Result](url))) shouldBe outcome
      }
    }

    "get" must {
      "get with the required des headers and return the result" in new Test with IfsTest {
        MockHttpClient
          .get(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            parameters = qps,
            requiredHeaders = requiredIfsHeaders,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.get(IfsUri[Result](url), queryParams = qps)) shouldBe outcome
      }
    }

    "delete" must {
      "delete with the required des headers and return the result" in new Test with IfsTest {
        MockHttpClient
          .delete(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            requiredHeaders = requiredIfsHeaders,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.delete(IfsUri[Result](url))) shouldBe outcome
      }
    }

    "put" must {
      "put with the required des headers and return result" in new Test with IfsTest {
        val requiredIfsHeadersPut: Seq[(String, String)] = requiredIfsHeaders ++ Seq("Content-Type" -> "application/json")
        MockHttpClient
          .put(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            body,
            requiredHeaders = requiredIfsHeadersPut,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.put(body, IfsUri[Result](url))) shouldBe outcome
      }
    }

    "content-type header already present and set to be passed through" must {
      "override (not duplicate) the value" when {
        testNoDuplicatedContentType("Content-Type" -> "application/user-type")
        testNoDuplicatedContentType("content-type" -> "application/user-type")

        def testNoDuplicatedContentType(userContentType: (String, String)): Unit =
          s"for user content type header $userContentType" in new Test with IfsTest {
            MockHttpClient
              .put(
                absoluteUrl,
                config = dummyHeaderCarrierConfig,
                body,
                requiredHeaders = requiredIfsHeaders ++ Seq("Content-Type" -> "application/json"),
                excludedHeaders = Seq(userContentType)
              )
              .returns(Future.successful(outcome))

            await(connector.put(body, IfsUri[Result](url))) shouldBe outcome
          }
      }
    }
  }

  "for TYS IFS" when {
    "post" must {
      "posts with the required tysIfs headers and returns the result" in new Test with TysIfsTest {
        val requiredTysIfsHeadersPost: Seq[(String, String)] = requiredTysIfsHeaders ++ Seq("Content-Type" -> "application/json")
        MockHttpClient
          .post(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            body,
            requiredHeaders = requiredTysIfsHeadersPost,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.post(body, TaxYearSpecificIfsUri[Result](url))) shouldBe outcome
      }
    }

    "get" must {
      "get with the required headers and return the result" in new Test with TysIfsTest {
        MockHttpClient
          .get(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            parameters = qps,
            requiredHeaders = requiredTysIfsHeaders,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.get(TaxYearSpecificIfsUri[Result](url), queryParams = qps)) shouldBe outcome
      }
    }

    "delete" must {
      "delete with the required headers and return the result" in new Test with TysIfsTest {
        MockHttpClient
          .delete(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            requiredHeaders = requiredTysIfsHeaders,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.delete(TaxYearSpecificIfsUri[Result](url))) shouldBe outcome
      }
    }

    "put" must {
      "put with the required headers and return result" in new Test with TysIfsTest {
        val requiredTysIfsHeadersPut: Seq[(String, String)] = requiredTysIfsHeaders ++ Seq("Content-Type" -> "application/json")
        MockHttpClient
          .put(
            absoluteUrl,
            config = dummyHeaderCarrierConfig,
            body,
            requiredHeaders = requiredTysIfsHeadersPut,
            excludedHeaders = Seq("AnotherHeader" -> "HeaderValue"))
          .returns(Future.successful(outcome))

        await(connector.put(body, TaxYearSpecificIfsUri[Result](url))) shouldBe outcome
      }
    }

    "content-type header already present and set to be passed through" must {
      "override (not duplicate) the value" when {
        testNoDuplicatedContentType("Content-Type" -> "application/user-type")
        testNoDuplicatedContentType("content-type" -> "application/user-type")

        def testNoDuplicatedContentType(userContentType: (String, String)): Unit =
          s"for user content type header $userContentType" in new Test with TysIfsTest {
            MockHttpClient
              .put(
                absoluteUrl,
                config = dummyHeaderCarrierConfig,
                body,
                requiredHeaders = requiredTysIfsHeaders ++ Seq("Content-Type" -> "application/json"),
                excludedHeaders = Seq(userContentType)
              )
              .returns(Future.successful(outcome))

            await(connector.put(body, TaxYearSpecificIfsUri[Result](url))) shouldBe outcome
          }
      }
    }
  }

}
