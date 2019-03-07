/*
 * (c) Copyright 2017. Worldline IT Services UK Ltd
 * All rights reserved.
 * Reproduction in whole or in part is prohibited without the prior written consent of the copyright owner
 */
package com.worldline.robustini.graphdb.config


/**
 * Scala trait for managing information from application config.
 *
 * @author Dhananjay Ghanwat
 */

trait ApplicationConfig {

  // Load the application.conf file from the class-path.
  val conf = com.typesafe.config.ConfigFactory.load
  val username = conf.getString("database.username")
  val password = conf.getString("database.password")
  val url = conf.getString("database.url")


}