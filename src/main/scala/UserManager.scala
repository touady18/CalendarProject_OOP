
import java.io.{FileWriter, PrintWriter}

class UserManager(
                   var first_name : String,
                   var last_name : String,
                   val login : String,
                   var password : String) {
  def AddUserToFile() : Unit = {
    val filePath = "src/main/scala/userManager.csv"

    // Add The new user to userManager.csv
    val fileWriter = new FileWriter(filePath, true) //true => append
    val printWriter = new PrintWriter(fileWriter)

    printWriter.println(f"$first_name,$last_name,$login,$password")
    printWriter.close()
    fileWriter.close()

  }

  def deleteUserManager() : Unit = {
    println("todo")
  }

  def updatePassword() : Unit = {
    println("todo")
  }
}