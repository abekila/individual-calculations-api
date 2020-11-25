/*
 * Copyright 2020 HM Revenue & Customs
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

package v1.fixtures.getAllowancesDeductionsAndReliefs

import play.api.libs.json.{JsValue, Json}

object AllowancesDeductionsAndReliefsResponseFixture {

  def backendJson(allowancesDeductionsAndReliefsResponse: JsValue, errorCount: Int = 0): JsValue = Json.obj(
    "data" -> Json.obj(
      "metadata" -> Json.obj(
        "id" -> "f2fb30e5-4ab6-4a29-b3c1-c7264259ff1c",
        "calculationErrorCount" -> errorCount
      ),
      "allowancesDeductionsAndReliefs" -> allowancesDeductionsAndReliefsResponse
    )
  )

  val allowancesDeductionsAndReliefsResponseJsonNonEmpty: JsValue = Json.parse(
    """
      |{
      |  "summary": {
      |    "totalAllowancesAndDeductions": 1,
      |    "totalReliefs": 1
      |  },
      |  "detail":{
      |    "foo": "bar"
      |  }
      |}
      |""".stripMargin
  )

  val allowancesDeductionsAndReliefsResponseJsonEmpty: JsValue = Json.parse(
    """
      |{
      |  "summary": {},
      |  "detail":{}
      |}
    """.stripMargin
  )

  val allowancesDeductionsAndReliefsJsonFromBackend: JsValue = backendJson(allowancesDeductionsAndReliefsResponseJsonNonEmpty)

  val allowancesDeductionsAndReliefsJsonWithErrorsFromBackend: JsValue = backendJson(allowancesDeductionsAndReliefsResponseJsonNonEmpty, errorCount = 1)

  val noAllowancesDeductionsAndReliefsExistJsonFromBackend: JsValue = backendJson(allowancesDeductionsAndReliefsResponseJsonEmpty)
}
