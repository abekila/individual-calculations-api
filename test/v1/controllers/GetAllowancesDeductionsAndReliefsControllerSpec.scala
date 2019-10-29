/*
 * Copyright 2019 HM Revenue & Customs
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

package v1.controllers

import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Result
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HeaderCarrier
import v1.fixtures.AllowancesDeductionsAndReliefsFixture
import v1.handling.RequestDefn
import v1.mocks.hateoas.MockHateoasFactory
import v1.mocks.requestParsers.MockGetCalculationParser
import v1.mocks.services.{MockEnrolmentsAuthService, MockMtdIdLookupService, MockStandardService}
import v1.models.errors._
import v1.models.hateoas.{HateoasWrapper, Link}
import v1.models.hateoas.Method.GET
import v1.models.outcomes.ResponseWrapper
import v1.models.request.{GetCalculationRawData, GetCalculationRequest}
import v1.models.response.CalculationWrapperOrError
import v1.models.response.getAllowancesDeductionsAndReliefs.AllowancesHateoasData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GetAllowancesDeductionsAndReliefsControllerSpec
  extends ControllerBaseSpec
    with MockEnrolmentsAuthService
    with MockMtdIdLookupService
    with MockGetCalculationParser
    with MockStandardService
    with MockHateoasFactory {

  trait Test {
    val hc = HeaderCarrier()

    val controller = new GetAllowancesDeductionsAndReliefsController(
      authService = mockEnrolmentsAuthService,
      lookupService = mockMtdIdLookupService,
      parser = mockGetCalculationParser,
      service = mockStandardService,
      hateoasFactory = mockHateoasFactory,
      cc = cc
    )

    MockedMtdIdLookupService.lookup(nino).returns(Future.successful(Right("test-mtd-id")))
    MockedEnrolmentsAuthService.authoriseUser()
  }

  private val nino = "AA123456A"
  private val calcId = "someCalcId"
  private val correlationId = "X-123"

  private val rawData = GetCalculationRawData(nino, calcId)
  private val requestData = GetCalculationRequest(Nino(nino), calcId)

  private def uri = s"/$nino/self-assessment/$calcId"

  private def queryUri = "/input/uri"

  val testHateoasLink = Link(href = "/foo/bar", method = GET, rel = "test-relationship")

  val linksJson: JsObject = Json.parse(
    """
      |{
      |    "links": [
      |      {
      |       "href": "/foo/bar",
      |       "method": "GET",
      |       "rel": "test-relationship"
      |      }
      |    ]
      |}
      |""".stripMargin).as[JsObject]

  "handleRequest" should {
    "return OK with the calculation" when {
      "happy path" in new Test {
        MockGetCalculationParser
          .parse(rawData)
          .returns(Right(requestData))

        MockStandardService
          .doService(RequestDefn.Get(uri), OK)
          .returns(
            Future.successful(
              Right(ResponseWrapper(
                correlationId,
                CalculationWrapperOrError.CalculationWrapper(AllowancesDeductionsAndReliefsFixture.allowancesDeductionsAndReliefsModel)))))

        MockHateoasFactory
          .wrap(AllowancesDeductionsAndReliefsFixture.allowancesDeductionsAndReliefsModel, AllowancesHateoasData(nino, calcId))
          .returns(HateoasWrapper(AllowancesDeductionsAndReliefsFixture.allowancesDeductionsAndReliefsModel, Seq(testHateoasLink)))

        val result: Future[Result] = controller.getAllowancesDeductionsAndReliefs(nino, calcId)(fakeGetRequest(queryUri))

        status(result) shouldBe OK
        contentAsJson(result) shouldBe AllowancesDeductionsAndReliefsFixture.allowancesDeductionsAndReliefsJson.deepMerge(linksJson)
        header("X-CorrelationId", result) shouldBe Some(correlationId)
      }
    }

    "return FORBIDDEN with the error message" when {
      "error count is greater than zero" in new Test {
        MockGetCalculationParser
          .parse(rawData)
          .returns(Right(requestData))

        MockStandardService
          .doService(RequestDefn.Get(uri), OK)
          .returns(Future.successful(Right(ResponseWrapper(correlationId, CalculationWrapperOrError.ErrorsInCalculation))))

        val result: Future[Result] = controller.getAllowancesDeductionsAndReliefs(nino, calcId)(fakeGetRequest(queryUri))

        status(result) shouldBe FORBIDDEN
        contentAsJson(result) shouldBe Json.toJson(RuleCalculationErrorMessagesExist)
        header("X-CorrelationId", result) shouldBe Some(correlationId)
      }
    }

    "return NOT_FOUND with the error message" when {
      "no allowances, deductions or reliefs exist" in new Test {
        MockGetCalculationParser
          .parse(rawData)
          .returns(Right(requestData))

        MockStandardService
          .doService(RequestDefn.Get(uri), OK)
          .returns(
            Future.successful(
              Right(ResponseWrapper(
                correlationId,
                CalculationWrapperOrError.CalculationWrapper(AllowancesDeductionsAndReliefsFixture.noAllowancesDeductionsAndReliefsExistModel)))))

        val result: Future[Result] = controller.getAllowancesDeductionsAndReliefs(nino, calcId)(fakeGetRequest(queryUri))

        status(result) shouldBe NOT_FOUND
        contentAsJson(result) shouldBe Json.toJson(NoAllowancesDeductionsAndReliefsExist)
        header("X-CorrelationId", result) shouldBe Some(correlationId)
      }
    }
  }
}