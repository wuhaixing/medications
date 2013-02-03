package models
    
import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current
    
case class Drug(id: Long, label: String)
    
    
object Drug{

    val drug = {
      get[Long]("id") ~ 
      get[String]("label") map {
        case id~label => Drug(id, label)
      }
    }
          
    def all(): List[Drug] = DB.withConnection { implicit c =>
      SQL("select * from drug").as(drug *)
    }
      
    def create(label: String) {
      DB.withConnection { implicit c =>
        SQL("insert into drug(label) values ({label})").on(
          'label -> label
        ).executeUpdate()
      }
    }
    
    def delete(id: Long) {
      DB.withConnection { implicit c =>
        SQL("delete from drug where id = {id}").on(
          'id -> id
        ).executeUpdate()
      }
    }      
}