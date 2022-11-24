/*
 * Copyright 2022 HM Revenue & Customs
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

package v3.models.response.triggerCalculation

import config.AppConfig
import play.api.libs.json._
import v3.hateoas.{HateoasLinks, HateoasLinksFactory}
import v3.models.domain.TaxYear
import v3.models.hateoas.{HateoasData, Link}

case class TriggerCalculationResponse(calculationId: String)

object TriggerCalculationResponse extends HateoasLinks {

  implicit val downstreamReads: Reads[TriggerCalculationResponse] = (JsPath \ "id").read[String].map(TriggerCalculationResponse.apply)
  implicit val vendorWrites: OWrites[TriggerCalculationResponse]  = Json.writes[TriggerCalculationResponse]

  implicit object LinksFactory extends HateoasLinksFactory[TriggerCalculationResponse, TriggerCalculationHateoasData] {

    override def links(appConfig: AppConfig, data: TriggerCalculationHateoasData): Seq[Link] =
      Seq(
        list(appConfig, data.nino, Some(data.taxYear), isSelf = false),
        retrieve(appConfig, data.nino, data.taxYear, data.calculationId)
      )

  }

}

case class TriggerCalculationHateoasData(nino: String, taxYear: TaxYear, finalDeclaration: Boolean, calculationId: String) extends HateoasData
