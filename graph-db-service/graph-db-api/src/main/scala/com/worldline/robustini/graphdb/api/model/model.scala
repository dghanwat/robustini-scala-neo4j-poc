package com.worldline.robustini.graphdb.api.model

import play.api.libs.json.Format
import play.api.libs.json.JsValue
import play.api.libs.json.Json

case class Cast(name: String, job: String, role: String)

case class MovieCast(title: String, cast: List[Cast])

case class Movie(title: String, released: Int, tagline: String)

case class Graph(nodes: Seq[Node], links: Seq[Relation])

case class Relation(source: Int, target: Int)

case class Node(title: String, label: String)

/**
  * Scala object for transforming sales summary from JSON to Object and vice versa.
  */
object Cast {
  implicit val format: Format[Cast] = Json.format[Cast]
  def fromJson(json: JsValue): Cast = Json.fromJson[Cast](json).get
  def toJson(cast: Cast): JsValue = Json.toJson(cast)
}

object MovieCast {
  implicit val format: Format[MovieCast] = Json.format[MovieCast]
  def fromJson(json: JsValue): MovieCast = Json.fromJson[MovieCast](json).get
  def toJson(ob: MovieCast): JsValue = Json.toJson(ob)
}

object Movie {
  implicit val format: Format[Movie] = Json.format[Movie]
  def fromJson(json: JsValue): Movie = Json.fromJson[Movie](json).get
  def toJson(ob: Movie): JsValue = Json.toJson(ob)
}

object Graph {
  implicit val format: Format[Graph] = Json.format[Graph]
  def fromJson(json: JsValue): Graph = Json.fromJson[Graph](json).get
  def toJson(ob: Graph): JsValue = Json.toJson(ob)
}

object Relation {
  implicit val format: Format[Relation] = Json.format[Relation]
  def fromJson(json: JsValue): Relation = Json.fromJson[Relation](json).get
  def toJson(ob: Relation): JsValue = Json.toJson(ob)
}

object Node {
  implicit val format: Format[Node] = Json.format[Node]
  def fromJson(json: JsValue): Node = Json.fromJson[Node](json).get
  def toJson(ob: Node): JsValue = Json.toJson(ob)
}
