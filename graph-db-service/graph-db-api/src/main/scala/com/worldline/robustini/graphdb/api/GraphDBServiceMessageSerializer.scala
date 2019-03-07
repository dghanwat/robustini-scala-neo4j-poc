/*
 * (c) Copyright 2017. Worldline IT Services UK Ltd
 * All rights reserved.
 * Reproduction in whole or in part is prohibited without the prior written consent of the copyright owner
 */
package com.worldline.robustini.graphdb.api

import scala.collection.immutable
import scala.util.control.NonFatal

import com.lightbend.lagom.scaladsl.api.deser.MessageSerializer.NegotiatedDeserializer
import com.lightbend.lagom.scaladsl.api.deser.MessageSerializer.NegotiatedSerializer
import com.lightbend.lagom.scaladsl.api.deser.StrictMessageSerializer
import com.lightbend.lagom.scaladsl.api.transport.DeserializationException
import com.lightbend.lagom.scaladsl.api.transport.MessageProtocol
import com.lightbend.lagom.scaladsl.api.transport.NotAcceptable
import com.lightbend.lagom.scaladsl.api.transport.UnsupportedMediaType

import akka.util.ByteString
import play.api.libs.json.JsString
import play.api.libs.json.Json

/**
 * Scala object acting as custom message serializer to support various message protocols.
 *
 * @author Dhananjay Ghanwat
 */
object GraphDBServiceMessageSerializer {

  val CT_TEXT_PLAIN = "text/plain"
  val CT_APPLICATION_JSON = "application/json"
  val CT_CHARSET = "utf-8"

  /**
   * NegotiatedSerializer for handling plain text Strings.
   */
  class PlainTextSerializer(val charset: String) extends NegotiatedSerializer[String, ByteString] {

    /**
     * The protocol method returns the protocol that this serializer serializes to.
     */
    override val protocol = MessageProtocol(Some(CT_TEXT_PLAIN), Some(charset))

    /**
     * The serialize method is a straight forward conversion from String to ByteString.
     */
    def serialize(s: String) = ByteString.fromString(s, charset)
  }

  /**
   * NegotiatedSerializer for handling JSON messages.
   */
  class JsonTextSerializer extends NegotiatedSerializer[String, ByteString] {

    /**
     * The protocol method returns the protocol that this serializer serializes to.
     */
    override val protocol = MessageProtocol(Some(CT_APPLICATION_JSON))

    /**
     * The serialize method uses Play JSON to convert string to a JSON string.
     */
    def serialize(s: String) = ByteString.fromString(Json.stringify(JsString(s)))
  }

  /**
   * NegotiatedDeserializer for handling plain text Strings.
   */
  class PlainTextDeserializer(val charset: String) extends NegotiatedDeserializer[String, ByteString] {

    /**
     * The deserialize method for straight forward conversion from ByteString to String.
     */
    def deserialize(bytes: ByteString) = bytes.decodeString(charset)
  }

  /**
   * NegotiatedDeserializer for handling JSON messages.
   */
  class JsonTextDeserializer extends NegotiatedDeserializer[String, ByteString] {

    /**
     * The deserialize method for ByteString to String conversion using Play JSON.
     */
    def deserialize(bytes: ByteString) = {
      try {
        Json.parse(bytes.iterator.asInputStream).as[String]
      } catch {
        case NonFatal(e) => throw DeserializationException(e)
      }
    }
  }

  /**
   * StrictMessageSerializer to handle the request and response serialization.
   */
  class TextMessageSerializer extends StrictMessageSerializer[String] {

    /**
     * List of message protocols accepted for the response.
     */
    override def acceptResponseProtocols = List(
      MessageProtocol(Some(CT_TEXT_PLAIN)),
      MessageProtocol(Some(CT_APPLICATION_JSON)))

    /**
     * This is used by the client to determine which serializer to use for the request.
     * Because at this stage, no communication has happened between the server and the client,
     * no negotiation can be done, so the client just chooses a default serializer,
     * in this case, a utf-8 plain text serializer.
     */
    def serializerForRequest = new JsonTextSerializer

    /**
     * This is used both by the server to select the deserializer for the request,
     * and the client to select deserializer for the response. The passed in MessageProtocol
     * is the content type that was sent with the request or response, and we need to inspect
     * it to see if it’s a content type that we can deserialize, and return the appropriate content type.
     */
    def deserializer(protocol: MessageProtocol) = {
      protocol.contentType match {
        case Some(CT_TEXT_PLAIN) | None =>
          new PlainTextDeserializer(protocol.charset.getOrElse(CT_CHARSET))
        case Some(CT_APPLICATION_JSON) =>
          new JsonTextDeserializer
        case _ =>
          throw UnsupportedMediaType(protocol, MessageProtocol(Some(CT_TEXT_PLAIN)))
      }
    }

    /**
     * This takes the list of accepted protocols, as sent by the client, and selects one to use to serialize the response.
     * If it can’t find one that it supports, it throws an exception. Note here that an empty value for any property
     * means that the client is willing to accept anything, likewise if the client didn’t specify any accept protocols.
     */
    def serializerForResponse(accepted: immutable.Seq[MessageProtocol]) = {
      accepted match {
        case Nil => new PlainTextSerializer(CT_CHARSET)
        case protocols => {
          protocols.collectFirst {
            case MessageProtocol(Some(CT_TEXT_PLAIN | "text/*" | "*/*" | "*"), charset, _) =>
              new PlainTextSerializer(charset.getOrElse(CT_CHARSET))
            case MessageProtocol(Some(CT_APPLICATION_JSON), _, _) =>
              new JsonTextSerializer
          }.getOrElse {
            throw NotAcceptable(accepted, MessageProtocol(Some(CT_TEXT_PLAIN)))
          }
        }
      }
    }
  }
}