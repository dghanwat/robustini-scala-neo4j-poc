/*
 * (c) Copyright 2017. Worldline IT Services UK Ltd
 * All rights reserved.
 * Reproduction in whole or in part is prohibited without the prior written consent of the copyright owner
 */
package com.worldline.robustini.graphdb.impl

import play.api.libs.json.Json

object ServiceStatusResponse {

  def getServiceStatusResponse(isAlive: Boolean) = {
    Json.parse(
      s"""|{
          |"isAlive":${isAlive},
          |"version":"${BuildInfo.version}"
          |}
        """.stripMargin)
  }
}
