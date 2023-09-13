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

package v5.models.response.retrieveCalculation.calculation.taxCalculation

import play.api.libs.json.JsValue
import support.UnitSpec
import v5.models.utils.JsonErrorValidators

class BusinessAssetsDisposalsAndInvestorsRelSpec extends UnitSpec with JsonErrorValidators with TaxCalculationFixture {

  "have the correct fields optional" when {
    val json = (taxCalculationDownstreamJson \ "capitalGainsTax" \ "businessAssetsDisposalsAndInvestorsRel").as[JsValue]

    testJsonAllPropertiesOptional[BusinessAssetsDisposalsAndInvestorsRel](json)
  }

}
