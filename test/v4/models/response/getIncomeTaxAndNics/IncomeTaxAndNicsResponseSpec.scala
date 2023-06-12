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

package v4.models.response.getIncomeTaxAndNics

import api.models.hateoas.Link
import mocks.MockAppConfig
import play.api.libs.json.Json
import support.UnitSpec
import v4.fixtures.getIncomeTaxAndNics.IncomeTaxAndNicsResponseFixture._
import v4.hateoas.HateoasFactory
import api.models.hateoas.Method.GET
import v4.models.hateoas.HateoasWrapper

class IncomeTaxAndNicsResponseSpec extends UnitSpec with MockAppConfig {

  "IncomeTaxNicsResponse" when {
    "read from valid JSON" should {
      "produce the expected IncomeTaxNicsResponse object" in {
        incomeTaxAndNicsResponseTopLevelJson.as[IncomeTaxAndNicsResponse] shouldBe incomeTaxAndNicsResponseModel
      }
    }

    "written to JSON" should {
      "produce the expected JsObject" in {
        Json.toJson(incomeTaxAndNicsResponseModel) shouldBe incomeTaxNicsResponseJson
      }
    }
  }

  "LinksFactory" when {
    class Test extends MockAppConfig {
      val hateoasFactory = new HateoasFactory(mockAppConfig)
      val nino: String   = "someNino"
      val calcId: String = "calcId"
      MockAppConfig.apiGatewayContext.returns("individuals/calculations").anyNumberOfTimes()
    }

    "wrapping a IncomeTaxAndNicsResponse object" should {
      "expose the correct hateoas links" in new Test {
        hateoasFactory.wrap(incomeTaxAndNicsResponseModel, IncomeTaxAndNicsHateoasData(nino, calcId)) shouldBe
          HateoasWrapper(
            incomeTaxAndNicsResponseModel,
            Seq(
              Link(s"/individuals/calculations/$nino/self-assessment/$calcId", GET, "metadata"),
              Link(s"/individuals/calculations/$nino/self-assessment/$calcId/income-tax-nics-calculated", GET, "self")
            )
          )
      }
    }
  }

}