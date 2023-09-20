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

package v4.controllers.requestParsers

import api.controllers.requestParsers.RequestParser
import api.models.domain.{CalculationId, Nino, TaxYear}
import v4.controllers.requestParsers.validators.SubmitFinalDeclarationValidator
import v4.models.request.{SubmitFinalDeclarationRawData, SubmitFinalDeclarationRequest}

import javax.inject.Inject

class SubmitFinalDeclarationParser @Inject()(val validator: SubmitFinalDeclarationValidator)
    extends RequestParser[SubmitFinalDeclarationRawData, SubmitFinalDeclarationRequest] {

  override protected def requestFor(data: SubmitFinalDeclarationRawData): SubmitFinalDeclarationRequest = {
    SubmitFinalDeclarationRequest(Nino(data.nino), TaxYear.fromMtd(data.taxYear), CalculationId(data.calculationId))
  }

}