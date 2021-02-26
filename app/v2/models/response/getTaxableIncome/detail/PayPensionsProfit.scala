/*
 * Copyright 2021 HM Revenue & Customs
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

package v2.models.response.getTaxableIncome.detail

import play.api.libs.json._

case class PayPensionsProfit(incomeReceived: BigInt,
                             taxableIncome: BigInt,
                             totalSelfEmploymentProfit: Option[BigInt],
                             totalPropertyProfit: Option[BigInt],
                             totalFHLPropertyProfit: Option[BigInt],
                             totalUKOtherPropertyProfit: Option[BigInt],
                             totalForeignPropertyProfit: Option[BigInt],
                             totalEeaFhlProfit: Option[BigInt],
                             totalOccupationalPensionIncome: Option[BigDecimal],
                             totalStateBenefitsIncome: Option[BigDecimal],
                             totalBenefitsInKind: Option[BigDecimal],
                             totalPayeEmploymentAndLumpSumIncome: Option[BigDecimal],
                             totalEmploymentExpenses: Option[BigDecimal],
                             totalSeafarersDeduction: Option[BigDecimal],
                             totalForeignTaxOnForeignEmployment: Option[BigDecimal],
                             totalEmploymentIncome: Option[BigInt],
                             totalShareSchemesIncome: Option[BigDecimal],
                             totalOverseasPensionsStateBenefitsRoyalties: Option[BigDecimal],
                             totalAllOtherIncomeReceivedWhilstAbroad: Option[BigDecimal],
                             totalOverseasIncomeAndGains: Option[BigDecimal],
                             totalForeignBenefitsAndGifts: Option[BigDecimal],
                             businessProfitAndLoss: Option[BusinessProfitAndLoss])

object PayPensionsProfit {
  implicit val format: OFormat[PayPensionsProfit] = Json.format[PayPensionsProfit]
}