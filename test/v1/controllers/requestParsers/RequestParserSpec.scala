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

package v1.controllers.requestParsers

import play.api.http.Status.BAD_REQUEST
import support.UnitSpec
import uk.gov.hmrc.domain.Nino
import v1.controllers.requestParsers.validators.Validator
import v1.models.errors.{BadRequestError, ErrorWrapper, MtdErrors, NinoFormatError, RuleIncorrectOrEmptyBodyError}
import v1.models.request.RawData

class RequestParserSpec extends UnitSpec {

  private val nino = "AA123456A"
  case class Raw(nino: String) extends RawData
  case class Request(nino: Nino)

  trait Test {
    test =>

    val validator: Validator[Raw]

    val parser = new RequestParser[Raw, Request] {
      val validator = test.validator

      protected def requestFor(data: Raw) = Request(Nino(data.nino))
    }
  }

  "parse" should {
    "return a Request" when {
      "the validator returns no errors" in new Test {
        lazy val validator: Validator[Raw] = new Validator[Raw] {
          def validate(data: Raw) = Nil
        }

        parser.parseRequest(Raw(nino)) shouldBe Right(Request(Nino(nino)))
      }
    }

    "return a single error" when {
      "the validator returns a single error" in new Test {
        lazy val validator: Validator[Raw] = new Validator[Raw] {
          def validate(data: Raw) = List(NinoFormatError)
        }

        parser.parseRequest(Raw(nino)) shouldBe Left(ErrorWrapper(None, MtdErrors(BAD_REQUEST, NinoFormatError)))
      }
    }

    "return multiple errors" when {
      "the validator returns multiple errors" in new Test {
        lazy val validator: Validator[Raw] = new Validator[Raw] {
          def validate(data: Raw) = List(NinoFormatError, RuleIncorrectOrEmptyBodyError)
        }

        parser.parseRequest(Raw(nino)) shouldBe Left(ErrorWrapper(None,
          MtdErrors(BAD_REQUEST, BadRequestError, Some(Seq(NinoFormatError, RuleIncorrectOrEmptyBodyError)))))
      }
    }
  }

}
