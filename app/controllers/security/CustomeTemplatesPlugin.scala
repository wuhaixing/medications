package controllers.security

import securesocial.controllers._

import play.api.mvc.{RequestHeader, Request}
import play.api.templates.Html
import play.api.{Logger, Plugin, Application}
import securesocial.core.{Identity, SecuredRequest, SocialUser}
import play.api.data.Form
import securesocial.controllers.Registration.RegistrationInfo
import securesocial.controllers.PasswordChange.ChangeInfo
import views._

class CustomeTemplatesPlugin(application: Application) extends DefaultTemplatesPlugin(application) {

  override def getSignUpPage[A](implicit request: Request[A], form: Form[RegistrationInfo], token: String): Html = {
    html.security.Registration.signUp(form, token)
  }
}



