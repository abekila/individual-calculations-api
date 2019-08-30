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

package v1.models.response.getCalculationMetadata

import play.api.libs.json.Json
import support.UnitSpec
import v1.models.response.common.{CalculationReason, CalculationRequestor, CalculationType}

class CalculationMetadataSpec extends UnitSpec {

  val jsonInput = Json.parse("""{
                          |  "metadata": {
                          |    "id": "f2fb30e5-4ab6-4a29-b3c1-c7264259ff1c",
                          |    "taxYear": "2018-19",
                          |    "requestedBy": "customer",
                          |    "calculationReason": "customerRequest",
                          |    "calculationTimestamp": "2019-11-15T09:35:15.094Z",
                          |    "calculationType": "crystallisation",
                          |    "intentToCrystallise": true,
                          |    "crystallised": false,
                          |    "calculationErrorCount": 123
                          |  }
                          |}
                          |""".stripMargin)

  val jsonOutput = Json.parse("""{
                           |    "id": "f2fb30e5-4ab6-4a29-b3c1-c7264259ff1c",
                           |    "taxYear": "2018-19",
                           |    "requestedBy": "customer",
                           |    "calculationReason": "customerRequest",
                           |    "calculationTimestamp": "2019-11-15T09:35:15.094Z",
                           |    "calculationType": "crystallisation",
                           |    "intentToCrystallise": true,
                           |    "crystallised": false,
                           |    "calculationErrorCount": 123
                           |  }
                           |""".stripMargin)

  val metadata = CalculationMetadata(
    id = "f2fb30e5-4ab6-4a29-b3c1-c7264259ff1c",
    taxYear = "2018-19",
    requestedBy = CalculationRequestor.customer,
    calculationReason = CalculationReason.customerRequest,
    calculationTimestamp = "2019-11-15T09:35:15.094Z",
    calculationType = CalculationType.crystallisation,
    intentToCrystallise = true,
    crystallised = false,
    calculationErrorCount = Some(123)
  )

  "GetCalculationMetatdata" when {
    "written to JSON" should {
      "take the form specified" in {
        Json.toJson[CalculationMetadata](metadata) shouldBe jsonOutput
      }
    }

    "read from json" should {
      "read from metadata top level field" in {
        jsonInput.as[CalculationMetadata] shouldBe metadata
      }
    }
  }
}