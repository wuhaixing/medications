package models
    
case class Drug(id: Long, label: String)
    
object Drug{
      
    def all(): List[Drug] = Nil
      
    def create(label: String) {}
      
    def delete(id: Long) {}
      
}