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

package definition

import definition.APIStatus.APIStatus
import javax.inject.{Inject, Singleton}
import play.api.Logger
import config.{AppConfig, FeatureSwitch}
import Versions._

@Singleton
class ApiDefinitionFactory @Inject()(appConfig: AppConfig) {

  private val readScope = "read:self-assessment"
  private val writeScope = "write:self-assessment"

  lazy val definition: Definition =
    Definition(
      scopes = Seq(
        Scope(
          key = readScope,
          name = "#name#",
          description = "#desc#"
        ),
        Scope(
          key = writeScope,
          name = "#name#",
          description = "#desc#"
        )
      ),
      api = APIDefinition(
        name = "#mtd-api# (MTD)",
        description = "#desc#",
        context = appConfig.apiGatewayContext,
        versions = Seq(
          APIVersion(version = VERSION_1, access = buildWhiteListingAccess(), status = buildAPIStatus(VERSION_1), endpointsEnabled = true)
          //          ,
          //          APIVersion(version = VERSION_2, access = buildWhiteListingAccess(), status = buildAPIStatus(VERSION_2), endpointsEnabled = true)
        ),
        requiresTrust = None
      )
    )

  private[definition] def buildAPIStatus(version: String): APIStatus = {
    APIStatus.values
      .find(_.toString == appConfig.apiStatus(version))
      .getOrElse {
        Logger.error(s"[ApiDefinition][buildApiStatus] no API Status found in config.  Reverting to Alpha")
        APIStatus.ALPHA
      }
  }

  private[definition] def buildWhiteListingAccess(): Option[Access] = {
    val featureSwitch = FeatureSwitch(appConfig.featureSwitch)
    if (featureSwitch.isWhiteListingEnabled) Some(Access("PRIVATE", featureSwitch.whiteListedApplicationIds)) else None
  }
}
