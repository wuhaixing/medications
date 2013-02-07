package models
    
import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current
    
case class Drug(id: Pk[Long] = NotAssigned, label: String,zhunzi:String,company:String,basedCode:String)
    
/**
 * Helper for pagination.
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}
    
object Drug{

    val simple = {
      get[Pk[Long]]("id") ~ 
      get[String]("label") ~
      get[String]("zhunzi") ~
      get[String]("company") ~
      get[String]("basedCode")  map {
        case id~label~zhunzi~company~basedCode => Drug(id, label,zhunzi,company,basedCode)
      }
    }
          
   /**
   * Return a page of Drug.
   *
   * @param page Page to display
   * @param pageSize Number of drugs per page
   * @param orderBy Drug property used for sorting
   * @param filter Filter applied on the name column
   */
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"):Page[Drug] = {
    
    val offest = pageSize * page
    
    DB.withConnection { implicit connection =>
      
      val drugs = SQL(
        """
          select * from drug 
          where label like {filter}
          order by {orderBy} nulls last
          limit {pageSize} offset {offset}
        """
      ).on(
        'pageSize -> pageSize, 
        'offset -> offest,
        'filter -> filter,
        'orderBy -> orderBy
      ).as(Drug.simple *)

      val totalRows = SQL(
        """
          select count(*) from drug 
          where label like {filter}
        """
      ).on(
        'filter -> filter
      ).as(scalar[Long].single)

      Page(drugs, page, offest, totalRows)
      
    }
    
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
    
    def count() = {
      DB.withConnection { implicit connection =>
        SQL("select count(*) from drug").as(scalar[Long].single)
      }
    }     
}