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

package v1.models.domain

import play.api.libs.json.Json
import support.UnitSpec

class TriggerCalculationSpec extends UnitSpec {

  val json = Json.parse(
    """{
      |  "taxYear" : "2018-19"
      |}""".stripMargin)

  val response = TriggerCalculation("2018-19")

  "JSON writes" must {
    "align with spec" in {
      Json.toJson(response) shouldBe json
    }
  }

  "JSON reads" must {
    "align with back-end response" in {
      json.as[TriggerCalculation] shouldBe response
    }
  }

}
