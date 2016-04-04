package controllers

import models.Item
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc.{Action, Controller}

object Items extends Controller {
  val shop = models.Shop // Refer to your Shop implementation

  case class CreateItem(name: String, price: Double)

  implicit val readsCreateItem = Json.reads[CreateItem]

  implicit val writesItem = Json.writes[Item]


  val list = Action {
    Ok(Json.toJson(shop.list))
  }

  val create = Action(parse.json) { implicit request =>
    request.body.validate[CreateItem] match {
    case JsSuccess(createItem, _) =>
      shop.create(createItem.name, createItem.price) match {
        case Some(item) => Ok(Json.toJson(item))
        case None => InternalServerError
      }
    case JsError(errors) =>
      BadRequest
    }
  }

  def details(id: Long) = Action {
    shop.get(id) match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }
  def update(id: Long) = Action {NotImplemented}
  def delete(id: Long) = Action {NotImplemented}
}
