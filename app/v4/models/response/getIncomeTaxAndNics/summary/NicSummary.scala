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

package v4.models.response.getIncomeTaxAndNics.summary

import play.api.libs.json._

case class NicSummary(class2NicsAmount: Option[BigDecimal], class4NicsAmount: Option[BigDecimal], totalNic: Option[BigDecimal])

object NicSummary {
  implicit val format: OFormat[NicSummary] = Json.format[NicSummary]
}