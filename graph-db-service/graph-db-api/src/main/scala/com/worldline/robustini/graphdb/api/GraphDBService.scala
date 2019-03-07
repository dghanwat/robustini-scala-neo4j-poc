/*
 * (c) Copyright 2017. Worldline IT Services UK Ltd
 * All rights reserved.
 * Reproduction in whole or in part is prohibited without the prior written consent of the copyright owner
 */
package com.worldline.robustini.graphdb.api

import com.lightbend.lagom.scaladsl.api.Service
import com.lightbend.lagom.scaladsl.api.Service.named
import com.lightbend.lagom.scaladsl.api.Service.restCall
import com.lightbend.lagom.scaladsl.api.ServiceAcl
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.Method
import akka.NotUsed
import com.worldline.robustini.graphdb.api.GraphDBServiceMessageSerializer.TextMessageSerializer
import play.api.libs.json.JsValue


/**
  * <p>The service is based on the Lagom Library provided by Lightbend to develop microservices.
  * This describes everything that Lagom needs to know about how to serve and
  * consume the Graph DB Service.</p>
  *
  * @author Dhananjay Ghanwat
  */
trait GraphDBService extends Service {


  def status(): ServiceCall[NotUsed, JsValue];

  def getMovieByTitle(
                       title: String): ServiceCall[NotUsed, JsValue]

  /**
    * Method to override the default descriptor provided by Service interface to define the MetadataService.
    * <p>Here we have used <code>restCall</code> which is REST identifier for creating
    * Semantic REST APIs for MetadataService.</p>
    *
    * @return
    */
  override final def descriptor = {
    named("graphdb-service").withCalls(
      restCall(Method.GET, "/api/graphdbservice/status", status _),
      restCall(
        Method.GET,
        "/api/graphdbservice/movie/:title",
        getMovieByTitle _))
      .withAutoAcl(true)
      .withAcls(ServiceAcl.forMethodAndPathRegex(Method.OPTIONS, "/api/.*"))
  }
}