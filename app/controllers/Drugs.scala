package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import anorm._

import models.Drug
import securesocial.core.{Identity, Authorization,SecureSocial}
import views._    

object Drugs extends Controller with SecureSocial{
  /**
   * This result directly redirect to the application home.
   */
  val Home = Redirect(routes.Drugs.list(0, 2, ""))
  
  val drugForm = Form(
              mapping(
                "id" -> ignored(NotAssigned:Pk[Long]),
                "label" -> nonEmptyText,
                "zhunzi" -> nonEmptyText,
                "company" -> nonEmptyText,
                "basedCode" -> nonEmptyText
                )(Drug.apply)(Drug.unapply)
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

  /**
   * Display the 'edit form' of a existing Drug.
   *
   * @param id Id of the drug to edit
   */
  def edit(id: Long) = SecuredAction  { implicit request =>
          {
              Drug.findById(id).map { drug =>
                      Ok(html.editForm(id, drugForm.fill(drug)))
                    }.getOrElse(NotFound)
          }
  }

  /**
   * Handle the 'edit form' submission 
   *
   * @param id Id of the computer to edit
   */
  def update(id: Long) = Action { implicit request =>
    drugForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.editForm(id, formWithErrors)),
      drug => {
        Drug.update(id, drug)
        Home.flashing("success" -> "Drug %s has been updated".format(drug.label))
      }
    )
  }
  
      
  def add =  Action {
    Ok(html.createForm(drugForm))
  }
  
  /**
   * Handle the 'new computer form' submission.
   */
  def save = Action { implicit request =>
    drugForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.createForm(formWithErrors)),
      drug => {
        Drug.insert(drug)
        Home.flashing("success" -> "drug %s has been created".format(drug.label))
      }
    )
  }
      
  def delete(id: Long) = Action {
     Drug.delete(id)
     Home.flashing("success" -> "Drug has been deleted")
   }

    
    
}