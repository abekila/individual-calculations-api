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

package v5.models.response.retrieveCalculation.calculation.lossesAndClaims

import api.models.domain.TaxYear
import play.api.libs.json.{Format, Json, OFormat}
import v5.models.response.common.IncomeSourceType

case class UnclaimedLoss(
    incomeSourceId: Option[String],
    incomeSourceType: IncomeSourceType,
    taxYearLossIncurred: TaxYear,
    currentLossValue: BigInt,
    lossType: Option[String]
)

object UnclaimedLoss {

  implicit val incomeSourceTypeFormat: Format[IncomeSourceType] = IncomeSourceType.formatRestricted(
    IncomeSourceType.`self-employment`,
    IncomeSourceType.`uk-property-non-fhl`,
    IncomeSourceType.`foreign-property-fhl-eea`,
    IncomeSourceType.`uk-property-fhl`,
    IncomeSourceType.`foreign-property`
  )

  implicit val taxYearFormat: Format[TaxYear] = TaxYear.downstreamIntToMtdFormat

  implicit val format: OFormat[UnclaimedLoss] = Json.format[UnclaimedLoss]

}
