

import scala.io.StdIn.readLine
import java.nio.file.{Files, Paths}
import java.io.{File, PrintWriter}
import scala.collection.mutable.ListBuffer
import scala.io.Source
import java.io.{FileWriter, PrintWriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object Main {
  //Create a new user and create his calendar
  def create_user() : UserManager = {
    println("************************************************************************")
    println("Hello and welcome to 'Calendar time' app")
    print("insert your first name: ")
    var first_name = readLine()
    print("insert your last name: ")
    var last_name = readLine()
    print("insert a login: ")
    val login = readLine()
    // Check if the login already exists or not (to do in a second time)
    print("insert a password: ")
    var password = readLine()
    // Create the new user
    val newUser = new UserManager(first_name, last_name, login, password)
    println("Your account has been successfully created, thank you for your trust")
    println("************************************************************************")

    // Create User calendar
    var new_calendar = new Calendar(login, ListBuffer())
    new_calendar.createCalendar()
    newUser
  }

  // Check if the user exists and if the password is correct
  def access_auth(login : String, password : String, filename : String) : Boolean = {
    var login_ok = false
    // Read file
    val bufferSource = Source.fromFile(filename)
    for (line <- bufferSource.getLines().drop(1)) {
      //drop(1) ignore headers
      var cols = line.split(",").map(_.trim)
      if (cols(2).contains(login) && cols(3) == password) {
        login_ok = true
      }
    }
    login_ok
  }

  def addEvent(login_user : String) : Unit = {
    // Search the calendar
    val filePath = f"src/main/scala/calendarTime/$login_user.csv"
    print("Please enter event name : ")
    var event_name = readLine()

    print("Please enter the date (format : yyyy-MM-dd HH:mm) : ")
    var event_date = readLine()

    print("Please enter the location of the event : ")
    var event_location = readLine()

    // parse date
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val parsedDate: LocalDateTime = LocalDateTime.parse(event_date, formatter)
    //Add The new event
    val fileWriter = new FileWriter(filePath, true) //true => append
    val printWriter = new PrintWriter(fileWriter)

    printWriter.println(f"$event_name,$parsedDate,$event_location")
    printWriter.close()
    fileWriter.close()
  }
  def printEvents(login_user : String) : Unit = {
    val filePath = f"src/main/scala/calendarTime/$login_user.csv"
    val lines = Source.fromFile(filePath).getLines().toList

    for (line <- lines){
      println(line)
    }

    Source.fromFile(filePath).close()
  }

  def deleteEvents(login_user : String) : Unit = {
    val filePath = f"src/main/scala/calendarTime/$login_user.csv"
    val lines = Source.fromFile(filePath).getLines().toList

    println("Here is the list of all your events, please choose the one you want to delete : ")
    printEvents(login_user)
    println("************************")
    print("Please insert the event name: ")
    var event = readLine()
    val filtredLines = lines.head :: lines.tail.filterNot(_.contains(event))
    // rewrite the file
    val writer = new PrintWriter(filePath)
    filtredLines.foreach(writer.println)
    writer.close()

    Source.fromFile(filePath).close()
    println("The line was deleted")

  }

  def modifyEvent(login_user : String) : Unit = {
    val filePath = f"src/main/scala/calendarTime/$login_user.csv"
    val lines = Source.fromFile(filePath).getLines().toList

    println("Here is the list of all your events, please choose the one you want to modify : ")
    printEvents(login_user)
    println("************************")
    print("Please insert the event name: ")
    var event = readLine()
    println("Please insert the new information")
    print("Do you want to change the event name ? Your answer: ")
    var newName = readLine()
    print("Do you want to change the date? Your answer: ")
    var newDate = readLine()
    print("Do you want to change the location? Your answer: ")
    var newLocation = readLine()

    // parse date
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val parsedDate: LocalDateTime = LocalDateTime.parse(newDate, formatter)

    val headers = lines.head
    val modifiedLine = lines.tail.map { line =>
      if (line.contains(event)) {
        val columns = line.split(",")
        val newLine = f"$newName, $parsedDate, $newLocation"
        newLine
      } else line
    }
    val writer = new PrintWriter(filePath)
    writer.println(headers)
    modifiedLine.foreach(writer.println)
    writer.close()

  }


  def menu_access(login_user : String) : Boolean = {
    var calendar_start = true
    println(f"Hi $login_user, welcome to your calendar time")
    while(calendar_start) {
      println("*")
      println("*")
      println("********************************************************************************")
      println("********************************************************************************")
      println("*")
      println("***************************** Principal menu ***********************************")
      println("*")
      println("Please choose an option")
      println("1/ Add a new event to my calendar ")
      println("2/ Display all recorded events ")
      println("3/ Delete an event ")
      println("4/ Modify an event ")
      println("5/ Quit ")

      readLine() match {
        case "1" => addEvent(login_user)
        case "2" => printEvents(login_user)
        case "3" => deleteEvents(login_user)
        case "4" => modifyEvent(login_user)
        case "5" =>
          calendar_start = false
          println("*******************************")
          println(f"Good bye $login_user, see you soon !")
          println("*******************************")
          println("*")
          println("*")
      }
    }
    calendar_start
  }

  def main(args: Array[String]): Unit = {
    // Create a file access if it doesn't exist yet

    val filePath = "src/main/scala/userManager.csv"
    if (!Files.exists(Paths.get(filePath))) {
      val file = new PrintWriter(new File(filePath))
      file.println("first_name,last_name,login,password")
      file.close()
    }
    // Ask the user if she/he already has an account
    println("*")
    println("*****************************************************************************")
    println("                           Welcome to Calendar Time")
    println("*****************************************************************************")
    println("Do you already have an account?")
    println("Please answer by yes or no : ")
    var calendar_start = true

    var response_expectation = true
    while (response_expectation) {
      print("Please enter 'yes' or 'no': ")
      // Delete spaces with trim
      val response = readLine().trim.toLowerCase

      response match {
        case "no" =>
          var new_user = create_user().AddUserToFile()
          response_expectation = false
        case "yes" =>
          // Checking on the file if the user exists
          response_expectation = false
        case _ =>
          println("Sorry, I didn't understand your answer, please enter 'yes' or 'non'. ")
      }
    }
    var session = true
    var cpt = 0
    var login_currentUser = ""
    while(session || cpt < 3 ) {
      // ask for a connection
      println("To access to your calendar, please log in : ")
      print("Insert your login: ")
      login_currentUser = readLine()
      print("Insert your password: ")
      var password_currentUser = readLine()

      if(access_auth(login_currentUser,password_currentUser, filePath)) {
        println("Yes the access is OK")
        calendar_start = true

        cpt = 3
        session = false
      }
      else {
        if(cpt < 3 ) {
          println("Sorry, we cannot find your login/password, please try again")
          cpt = cpt + 1
          session = false
          calendar_start = false
        }
      }
      if(cpt >= 3 && !calendar_start){
        println("You tried several time, we cannot find your login/password")
        println("re launch the app calendar time and create an account if you don't have one")
        // TODO -> Add an option for forget password
      }
    }
    while(calendar_start) {
      calendar_start = menu_access(login_currentUser)
    }
    println("You closed the Calendar time app")

  }
}