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

package v1.models.response.getTaxableIncome.detail.foreignProperty

import play.api.libs.json.{Json, OFormat}
import v1.models.response.getTaxableIncome.detail.foreignProperty.detail.BusinessSourceAdjustableSummary
import v1.models.response.getTaxableIncome.detail.foreignProperty.detail.LossClaimsDetail
import v1.models.response.getTaxableIncome.detail.foreignProperty.summary.LossClaimsSummary

case class ForeignProperty(totalIncome: Option[BigDecimal],
                           totalExpenses: Option[BigDecimal],
                           netProfit: Option[BigDecimal],
                           netLoss: Option[BigDecimal],
                           totalAdditions: Option[BigDecimal],
                           totalDeductions: Option[BigDecimal],
                           accountingAdjustments: Option[BigDecimal],
                           adjustedIncomeTaxLoss: Option[BigInt],
                           taxableProfit: Option[BigInt],
                           taxableProfitAfterIncomeTaxLossesDeduction: Option[BigInt],
                           lossClaimsSummary: Option[LossClaimsSummary],
                           lossClaimsDetail: Option[LossClaimsDetail],
                           bsas: Option[BusinessSourceAdjustableSummary]
                          )

object ForeignProperty {
  implicit val formats: OFormat[ForeignProperty] = Json.format[ForeignProperty]
}