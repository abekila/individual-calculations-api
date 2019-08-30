/*
 * Copyright 2019 HM Revenue & Customs
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

package v1.controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}
import v1.connectors.httpparsers.StandardHttpParser.SuccessCode
import v1.controllers.requestParsers.ListCalculationsParser
import v1.handling.{RequestDefn, RequestHandling}
import v1.models.errors._
import v1.models.outcomes.ResponseWrapper
import v1.models.request.{ListCalculationsRawData, ListCalculationsRequest}
import v1.models.response.listCalculations.ListCalculationsResponse
import v1.services._

import scala.concurrent.ExecutionContext

@Singleton
class ListCalculationsController @Inject()(
    authService: EnrolmentsAuthService,
    lookupService: MtdIdLookupService,
    listCalculationsParser: ListCalculationsParser,
    service: StandardService,
    cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends StandardController[ListCalculationsRawData, ListCalculationsRequest, ListCalculationsResponse, ListCalculationsResponse, AnyContent](
      authService,
      lookupService,
      listCalculationsParser,
      service,
      cc) {
  controller =>

implicit  val endpointLogContext: EndpointLogContext =
    EndpointLogContext(
      controllerName = "ListCalculationsController",
      endpointName = "listCalculations"
    )

  override val successCode: SuccessCode = SuccessCode(OK)

  override def requestHandlingFor(playRequest: Request[AnyContent],
                                  req: ListCalculationsRequest): RequestHandling[ListCalculationsResponse, ListCalculationsResponse] = {
    RequestHandling[ListCalculationsResponse](
      RequestDefn
        .Get(playRequest.path)
        .withOptionalParams("taxYear" -> req.taxYear))
      .withPassThroughErrors(
        NinoFormatError,
        TaxYearFormatError,
        RuleTaxYearNotSupportedError,
        RuleTaxYearRangeExceededError,
        NotFoundError,
        DownstreamError
      )
      .mapSuccess(notFoundErrorWhenEmpty)
  }

  def listCalculations(nino: String, taxYear: Option[String]): Action[AnyContent] =
    authorisedAction(nino).async { implicit request =>
      val rawData = ListCalculationsRawData(nino, taxYear)

      doHandleRequest(rawData)
    }

  private def notFoundErrorWhenEmpty(responseWrapper: ResponseWrapper[ListCalculationsResponse]) =
    responseWrapper.toErrorWhen {
      case response if response.calculations.isEmpty => MtdErrors(NOT_FOUND, NotFoundError)
    }
}