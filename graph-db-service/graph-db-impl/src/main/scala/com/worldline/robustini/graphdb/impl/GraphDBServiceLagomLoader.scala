/*
 * (c) Copyright 2017. Worldline IT Services UK Ltd
 * All rights reserved.
 * Reproduction in whole or in part is prohibited without the prior written consent of the copyright owner
 */
package com.worldline.robustini.graphdb.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServerComponents}
import com.softwaremill.macwire.wire
import com.worldline.robustini.graphdb.api.GraphDBService
import org.apache.logging.log4j.scala.Logging
import play.api.Environment
import play.api.libs.ws.ahc.{AhcWSClient, AhcWSComponents}
import play.api.mvc.EssentialFilter
import play.filters.cors.CORSComponents
import com.worldline.robustini.graphdb.config.ApplicationConfig
import com.worldline.robustini.graphdb.model.MovieService

import scala.concurrent.{ExecutionContext, Future}
import org.apache.logging.log4j.scala.Logger
import org.neo4j.driver.v1.{AuthTokens, GraphDatabase}
import neotypes.implicits._

/**
 * Lagom service application loader.
 *
 * @author Dhananjay Ghanwat
 */
class GraphDBServiceLagomLoader extends LagomApplicationLoader {

  val log: Logger = Logger(getClass)

  /**
   * Method to load the lagom Reporting service application using lagom application context.
   *
   * @param <code>com.lightbend.lagom.scaladsl.server.LagomApplicationContext</code>.
   */
  override def load(context: LagomApplicationContext): LagomApplication = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val application = new GraphDBServiceLagomApplication(context) {
      override def serviceLocator = ServiceLocator.NoServiceLocator
    }
    application
  }

  /**
   * Service descriptor for Reporting service.
   */
  override def describeService = Some(readDescriptor[GraphDBService])
}

/**
 * Lagom application for Service.
 */
abstract class GraphDBServiceLagomApplication(context: LagomApplicationContext)
  extends LagomApplication(context) with GraphDBServiceLagomComponents with AhcWSComponents with CORSComponents {
  override val httpFilters: Seq[EssentialFilter] = Seq(corsFilter)
}

/**
 * Lagom server components for service.
 */
trait GraphDBServiceLagomComponents extends LagomServerComponents with Logging with ApplicationConfig {

  // Scala execution context for asynchronous execution.
  implicit def executionContext: ExecutionContext

    // Play Framework class loader.
  def environment: Environment

  logger.info(s"user name is ${username}")
  logger.info(s"password is ${password}")
    val driver = GraphDatabase.driver(url, AuthTokens.basic(username,
    password))

  implicit def movieService = new MovieService(driver.asScala[Future])


  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[GraphDBService](wire[GraphDBServiceImpl])

  // Register the JSON serializer registry
  lazy val jsonSerializerRegistry = GraphDBServiceLagomSerializerRegistry
}