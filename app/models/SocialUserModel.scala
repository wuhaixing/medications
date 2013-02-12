package models

import securesocial.core.{UserId, Identity,SocialUser}
import _root_.java.util.{Date, UUID}
import org.joda.time.DateTime
import securesocial.core.providers.Token
import securesocial.core.AuthenticationMethod
import securesocial.core.PasswordInfo

import play.api.db._
import anorm._
import anorm.SqlParser._

import play.api.Play.current

import java.sql.Timestamp

object SocialUserModel{

    val socialUser = {
          get[String]("user_id") ~ 
          get[String]("provider_id") ~
          get[String]("first_name") ~
          get[String]("last_name") ~
          get[Option[String]]("email") ~
          get[Option[String]]("avatar_url") ~
          get[String]("hasher") ~
          get[String]("password") map {
            case user_id~provider_id~first_name~last_name~email~avatar_url~hasher~password => 
          SocialUser(
              UserId(user_id, provider_id),
              first_name, 
              last_name, 
              first_name + " " + last_name, 
              email,
              avatar_url, 
              AuthenticationMethod("userPassword"),
              None,
              None,
              Some(PasswordInfo("bcrypt", password, None))
           )
     }
  }

  val token = { 
          get[String]("uuid") ~ 
          get[String]("email") ~
          get[Date]("creation_time") ~
          get[Date]("expiration_time") ~
          get[Boolean]("is_sign_up") map {
            case uuid~email~creation_time~expiration_time~is_sign_up => 
          Token(
             uuid,email,new DateTime(creation_time),new DateTime(expiration_time),is_sign_up 
          )
        }
  }   
   
   /**
   * find user
   * 
   */
   def find(id: UserId) = {
    DB.withConnection { implicit c =>
      SQL(
  		  """
  		    select * from SOCIAL_USER
  		    where user_id = {user_id};
  		  """
      ).on("user_id" -> id.id).as(SocialUserModel.socialUser.singleOpt)
    }
   }
  
  /**
   * findByEmailAndProvider user
   * 
   */
  def findByEmailAndProvider(email: String, providerId: String): Option[SocialUser] = {
    
    DB.withConnection { implicit c =>
      SQL(
		  """
		    select * from SOCIAL_USER
		      where email = {email}
            and provider_id = {providerId}
		  """).on(
		      'email -> email,
		      'providerId -> providerId
		     ).as(SocialUserModel.socialUser.singleOpt)

    } 
  } 
  

  /**
   * save user
   * (actually save or update)
   * 
   */
  def save(user: Identity) {
      val socialUser = find(user.id)
      
      if (socialUser == None) { // user not exists
       DB.withConnection { implicit c => 
        // create a new user
        SQL(
          """
    		    insert into SOCIAL_USER 
        		  (user_id,provider_id,first_name,last_name,email,avatar_url,hasher,password)
        		values
        		  ({user_id},{provider_id},{first_name},{last_name},{email},{avatar_url},{hasher},{password})
    		  """).on(
            'user_id -> user.id.id,
            'provider_id -> user.id.providerId,
            'first_name -> user.firstName,
            'last_name -> user.lastName,
            'email -> user.email,
            'avatar_url -> user.avatarUrl,
            'hasher -> "bcrypt",
            'password -> user.passwordInfo.get.password).executeUpdate()
        }
      } else { // user exists
        // update the user
        DB.withConnection { implicit c =>
          SQL(
          """
    		    update SOCIAL_USER 
        		  set user_id= {user_id},
                  provider_id= {provider_id},
                  first_name= {first_name},
                  last_name= {last_name},
                  email= {email},
                  avatar_url= {avatar_url},
                  hasher= {hasher},
                  password= {password}
              where user_id = {user_id}
    		  """).on(
                'user_id -> user.id.id,
                'provider_id -> user.id.providerId,
                'first_name -> user.firstName,
                'last_name -> user.lastName,
                'email -> user.email,
                'avatar_url -> user.avatarUrl,
                'hasher -> "bcrypt",
                'password -> user.passwordInfo.get.password).executeUpdate()
      }
    } 
  } 
  

  /**
   * save token
   * 
   */
  def save(token: Token) {
    DB.withConnection { implicit c =>

  		// create a new token
  		val sqlQuery = SQL(
  		  """
  		    insert into TOKEN 
    			  (uuid, email, creation_time, expiration_time, is_sign_up)
    			values
    			  ({uuid}, {email},  parsedatetime({creation_time}, 'yyyy-MM-dd HH:mm:ss'), parsedatetime({expiration_time}, 'yyyy-MM-dd HH:mm:ss'), {is_sign_up})
  		  """).on(
  		    'uuid -> token.uuid,
  		    'email -> token.email,
  		    'creation_time -> token.creationTime.toString("yyyy-MM-dd HH:mm:ss"),
  		    'expiration_time -> token.expirationTime.toString("yyyy-MM-dd HH:mm:ss"),
  		    'is_sign_up -> token.isSignUp
  		    )
  
  		val result: Int = sqlQuery.executeUpdate()

    }
  } 
  

  /**
   * findToken
   * 
   */
  def findToken(token: String): Option[Token] = {

    DB.withConnection { implicit c =>
      
      SQL(
    		  """
    		    select * from TOKEN where uuid = {uuid};
    		  """).on("uuid" -> token).as(SocialUserModel.token.singleOpt)
    }
  } 
  

  /**
   * deleteToken
   * 
   */
  def deleteToken(uuid: String) {

    DB.withConnection { implicit c =>
      // delete token
      SQL(
    		"delete from TOKEN where uuid = {uuid};"
        ).on("uuid" -> uuid).executeUpdate()

    }
  }
  

  /**
   * deleteTokens
   * 
   */
  def deleteTokens() {
    
    DB.withConnection { implicit c =>
      // delete all tokens
      SQL("delete from TOKEN;").executeUpdate()
    }
  }
  

  /**
   * deleteExpiredTokens
   * 
   */
  def deleteExpiredTokens() {

    DB.withConnection { implicit c =>
      // delete expired tokens
      SQL("delete from TOKEN where EXPIRATION_TIME < NOW();"
           ).executeUpdate()

    }     
  } 
}     