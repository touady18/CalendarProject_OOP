import java.time.LocalDateTime

class Event(
             val id_event : Int,
             var event_name : String,
             var event_date: LocalDateTime,
             var event_location : String)
{

  def print_event() : Unit = {
    println(f"Event : $id_event / nom : $event_name - date : $event_date - location : $event_location ")
  }
}