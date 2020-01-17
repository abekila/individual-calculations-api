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

package v1.fixtures.getIncomeTaxAndNics.detail

import play.api.libs.json.{JsValue, Json}
import v1.models.response.getIncomeTaxAndNics.detail.TaxDeductedAtSource

object TaxDeductedAtSourceFixture {

  val ukLandAndProperty: Option[BigDecimal] = Some(100.25)
  val savings: Option[BigDecimal] = Some(200.25)

  val taxDeductedAtSourceModel: TaxDeductedAtSource =
    TaxDeductedAtSource(
      ukLandAndProperty = ukLandAndProperty,
      savings = savings
    )

  val taxDeductedAtSourceJson: JsValue = Json.parse(
    s"""
       |{
       |   "ukLandAndProperty" : ${ukLandAndProperty.get},
       |   "savings" : ${savings.get}
       |}
  """.stripMargin
  )
}