import scala.io.StdIn.readLine;
import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter.ofPattern;
import java.time.format.DateTimeFormatter;
import scala.collection.mutable.ListBuffer;
import java.nio.file.{Files, Paths}
import java.io.{File, PrintWriter}

class Calendar(
              val name : String,
              var eventList : ListBuffer[Event]
              ) {
  // Add events to the calendar
  def addEvents(oldList: ListBuffer[Event]): ListBuffer[Event] = {
    println("Please enter event name : ")
    var event_name = readLine()

    println("Please enter the date (format : yyyy-MM-dd HH:mm) : ")
    var event_date = readLine()

    println("Please enter the location of the event : ")
    var event_location = readLine()

    // parse date
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val parsedDate: LocalDateTime = LocalDateTime.parse(event_date, formatter)

    var new_id: Int = if (oldList.nonEmpty) oldList.last.id_event + 1 else 1

    // Create a new event
    val event = new Event(new_id, event_name, parsedDate, event_location)
    oldList += event // add the new event to the list

    println(f"A new event has been successfully added ! ID : $new_id")

    oldList
  }

  def createCalendar() : Unit = {
    var filePath = f"src/main/scala/calendarTime/$name.csv"
    if (!Files.exists(Paths.get(filePath))) {
      val file = new PrintWriter(new File(filePath))
      file.println("event_name,date,location")
      file.close()
    }
  }

}