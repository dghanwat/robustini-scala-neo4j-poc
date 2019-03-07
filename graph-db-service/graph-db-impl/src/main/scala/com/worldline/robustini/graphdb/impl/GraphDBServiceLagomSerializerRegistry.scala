/*
 * (c) Copyright 2017. Worldline IT Services UK Ltd
 * All rights reserved.
 * Reproduction in whole or in part is prohibited without the prior written consent of the copyright owner
 */
package com.worldline.robustini.graphdb.impl

import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry

/**
  * Reporting Service Lagom JSON Serializer Registry to be passed to Akka Actor System.
  *
  * @author Dhananjay Ghanwat
  */
object GraphDBServiceLagomSerializerRegistry extends JsonSerializerRegistry {

  /**
    * List of serializers for model objects.
    */
  override def serializers = List()
}
