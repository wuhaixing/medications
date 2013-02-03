package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Drug
    
object Drugs extends Controller {
  
  def index = Action {
    Ok(views.html.drugs(Drug.all(), drugForm))
  }
  
  def add= Action { implicit request =>
      drugForm.bindFromRequest.fold(
        errors => BadRequest(views.html.drugs(Drug.all(), errors)),
        label => {
          Drug.create(label)
          Redirect(routes.Drugs.index)
        }
      )
    }
      
  def delete(id: Long) = Action {
    Drug.delete(id)
    Redirect(routes.Drugs.index)
  }

    
    val drugForm = Form(
      "label" -> nonEmptyText
    )
}