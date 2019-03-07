/*
 * (c) Copyright 2017. Worldline IT Services UK Ltd
 * All rights reserved.
 * Reproduction in whole or in part is prohibited without the prior written consent of the copyright owner
 */
package com.worldline.robustini.graphdb.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator

import scala.concurrent.{Await, ExecutionContext, Future}
import org.apache.logging.log4j.scala.Logging
import com.lightbend.lagom.scaladsl.api.transport.{BadRequest, Forbidden, ResponseHeader}
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.worldline.robustini.graphdb.api.GraphDBService
import com.worldline.robustini.graphdb.model.MovieService
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.ahc.AhcWSClient

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

/**
  * Implementation class for <code>GraphDBService</code>.
  *
  * @author Dhananjay Ghanwat
  */
class GraphDBServiceImpl(
                          implicit
                          ec: ExecutionContext,
                          movieService:     MovieService) extends GraphDBService with Logging {

  /**
    * Returns the container version number, status and other information
    *
    * @return Container Status
    */
  override def status() =
    ServerServiceCall { (_, _) =>
      Future (ResponseHeader.Ok.withStatus(200), ServiceStatusResponse.getServiceStatusResponse(true))
    }

  /**
    * Implementation of getTransactionsByDevice service.
    */
  override def getMovieByTitle(
                                title: String) =
    ServerServiceCall { (_, _) =>
      logger.info(s"Getting transactions for device ${title}")
        movieService.findMovie(title).map {
          case Some(movie) =>
            (ResponseHeader.Ok.withStatus(200),Json.toJson(movie))
          case None =>
            (ResponseHeader.Ok.withStatus(400),Json.toJson(""))
        }
    }


}