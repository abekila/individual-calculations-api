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

package v4.models.response.getIncomeTaxAndNics.detail.nics

import support.UnitSpec
import v4.fixtures.getIncomeTaxAndNics.detail.nics.Class4LossesFixture._
import v4.models.utils.JsonErrorValidators

class Class4LossesSpec extends UnitSpec with JsonErrorValidators {

  testJsonProperties[Class4Losses](class4LossesJson)(
    mandatoryProperties = Seq(),
    optionalProperties = Seq(
      "totalClass4LossesAvailable",
      "totalClass4LossesUsed",
      "totalClass4LossesCarriedForward"
    )
  )

}