package service

import play.api.Application
import securesocial.core._
import securesocial.core.providers.Token
import securesocial.core.UserId
import scala.Some
import org.mindrot.jbcrypt.BCrypt
import models.SocialUserModel

/**
 * A Sample DatabaseUserService
 *
 */
class DatabaseUserService(application: Application) extends UserServicePlugin(application){
   /**
       * Finds a user that maches the specified id
       *
       * @param id the user id
       * @return an optional user
       */
      def find(id: UserId):Option[Identity] = {
        SocialUserModel.find(id)
      }
    
      /**
       * Finds a user by email and provider id.
       *
       * Note: If you do not plan to use the UsernamePassword provider just provide en empty
       * implementation.
       *
       * @param email - the user email
       * @param providerId - the provider id
       * @return
       */
      def findByEmailAndProvider(email: String, providerId: String):Option[Identity] =
      {
        SocialUserModel.findByEmailAndProvider(email, providerId)
      }
    
      /**
       * Saves the user.  This method gets called when a user logs in.
       * This is your chance to save the user information in your backing store.
       * @param user
       */
      def save(user: Identity) {
        SocialUserModel.save(user)
      }
    
      /**
       * Saves a token.  This is needed for users that
       * are creating an account in the system instead of using one in a 3rd party system.
       *
       * Note: If you do not plan to use the UsernamePassword provider just provide en empty
       * implementation
       *
       * @param token The token to save
       * @return A string with a uuid that will be embedded in the welcome email.
       */
      def save(token: Token) = {
        SocialUserModel.save(token)
      }
    
    
      /**
       * Finds a token
       *
       * Note: If you do not plan to use the UsernamePassword provider just provide an empty
       * implementation
       *
       * @param token the token id
       * @return
       */
      def findToken(token: String): Option[Token] = {
        SocialUserModel.findToken(token)
      }
    
      /**
       * Deletes a token
       *
       * Note: If you do not plan to use the UsernamePassword provider just provide an empty
       * implementation
       *
       * @param uuid the token id
       */
      def deleteToken(uuid: String) {
        SocialUserModel.deleteToken(uuid)
      }
    
      /**
       * Deletes all expired tokens
       *
       * Note: If you do not plan to use the UsernamePassword provider just provide an empty
       * implementation
       *
       */
      def deleteExpiredTokens() {
        SocialUserModel.deleteExpiredTokens
      }
}

