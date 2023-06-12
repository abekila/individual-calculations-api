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

package v4.models.response.getIncomeTaxAndNics.detail.capitalGainsTax

import play.api.libs.json.{Json, OFormat}

case class ResidentialPropertyAndCarriedInterest(
    gainsIncome: Option[BigDecimal],
    lossesBroughtForward: Option[BigDecimal],
    lossesArisingThisYear: Option[BigDecimal],
    gainsAfterLosses: Option[BigDecimal],
    annualExemptionAmount: Option[BigDecimal],
    taxableGains: Option[BigDecimal],
    cgtTaxBands: Option[Seq[CgtTaxBand]],
    totalTaxAmount: Option[BigDecimal]
)

object ResidentialPropertyAndCarriedInterest {
  implicit val format: OFormat[ResidentialPropertyAndCarriedInterest] = Json.format[ResidentialPropertyAndCarriedInterest]
}
