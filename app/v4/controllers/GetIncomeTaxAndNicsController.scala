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

package v4.controllers

import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}
import utils.IdGenerator
import v4.connectors.httpparsers.StandardHttpParser
import v4.connectors.httpparsers.StandardHttpParser.SuccessCode
import v4.controllers.requestParsers.GetCalculationParser
import v4.handler.{AuditHandler, RequestDefn, RequestHandler}
import v4.hateoas.HateoasFactory
import v4.models.audit.GenericAuditDetail
import v4.models.errors._
import v4.models.hateoas.HateoasWrapper
import v4.models.request.{GetCalculationRawData, GetCalculationRequest}
import v4.models.response.calculationWrappers.CalculationWrapperOrError
import v4.models.response.getIncomeTaxAndNics.{IncomeTaxAndNicsHateoasData, IncomeTaxAndNicsResponse}
import v4.services.{AuditService, EnrolmentsAuthService, MtdIdLookupService, StandardService}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class GetIncomeTaxAndNicsController @Inject() (authService: EnrolmentsAuthService,
                                               lookupService: MtdIdLookupService,
                                               parser: GetCalculationParser,
                                               service: StandardService,
                                               hateoasFactory: HateoasFactory,
                                               auditService: AuditService,
                                               cc: ControllerComponents,
                                               idGenerator: IdGenerator)(implicit ec: ExecutionContext)
    extends StandardController[
      GetCalculationRawData,
      GetCalculationRequest,
      CalculationWrapperOrError[IncomeTaxAndNicsResponse],
      HateoasWrapper[IncomeTaxAndNicsResponse],
      AnyContent](authService, lookupService, parser, service, auditService, cc, idGenerator) {
  controller =>

  implicit val endpointLogContext: EndpointLogContext =
    EndpointLogContext(
      controllerName = "GetIncomeTaxAndNicsController",
      endpointName = "getIncomeTaxAndNics"
    )

  override val successCode: StandardHttpParser.SuccessCode = SuccessCode(OK)

  override def requestHandlerFor(
      playRequest: Request[AnyContent],
      req: GetCalculationRequest): RequestHandler[CalculationWrapperOrError[IncomeTaxAndNicsResponse], HateoasWrapper[IncomeTaxAndNicsResponse]] =
    RequestHandler[CalculationWrapperOrError[IncomeTaxAndNicsResponse]](RequestDefn.Get(req.backendCalculationUri))
      .withPassThroughErrors(
        NinoFormatError,
        CalculationIdFormatError,
        NotFoundError,
        RuleIncorrectGovTestScenarioError
      )
      .mapSuccess { responseWrapper =>
        responseWrapper.mapToEither {
          case CalculationWrapperOrError.ErrorsInCalculation =>
            Left(ErrorWrapper(responseWrapper.correlationId, RuleCalculationErrorMessagesExist, None, FORBIDDEN))
          case CalculationWrapperOrError.CalculationWrapper(calc) => Right(calc)
        }
      }
      .mapSuccessSimple(rawResponse => hateoasFactory.wrap(rawResponse, IncomeTaxAndNicsHateoasData(req.nino.nino, rawResponse.id)))

  def getIncomeTaxAndNics(nino: String, calculationId: String): Action[AnyContent] =
    authorisedAction(nino).async { implicit request =>
      val rawData = GetCalculationRawData(nino, calculationId)

      val auditHandler: AuditHandler[GenericAuditDetail] = AuditHandler.withoutBody(
        "retrieveSelfAssessmentTaxCalculationIncomeTaxNicsCalculated",
        "retrieve-self-assessment-tax-calculation-income-tax-nics-calculated",
        Map("nino" -> nino, "calculationId" -> calculationId),
        request
      )

      doHandleRequest(rawData, Some(auditHandler))
    }

}