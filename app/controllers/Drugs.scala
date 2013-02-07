package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Drug
import views._    
object Drugs extends Controller {
  /**
   * This result directly redirect to the application home.
   */
  val Home = Redirect(routes.Drugs.list(0, 2, ""))
  
  val drugForm = Form(
                 "label" -> nonEmptyText
                 )   

  def index = Action { Home }
  
  /**
   * Display the paginated list of drugs.
   *
   * @param page Current page number (starts from 0)
   * @param orderBy Column to be sorted
   * @param filter Filter applied on drug names
   */
  def list(page: Int, orderBy: Int, filter: String) = Action { implicit request =>
    Ok(html.drugs(
      Drug.list(page = page, orderBy = orderBy, filter = ("%"+filter+"%")),
      orderBy, filter
    ))
  }
  
  def add = TODO
      
  def delete(id: Long) = Action {
     Drug.delete(id)
     Redirect(routes.Drugs.index)
   }

    
    
}