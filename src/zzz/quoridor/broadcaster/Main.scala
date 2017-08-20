package zzz.quoridor.broadcaster

object Main {

  val ABOUT = "Quoridor Server v1.4 (c) 2016, 2017 Atanas Georgiev\n"
  
  def main(args: Array[String]) {

    if (args.length < 1) {
      println("Usage: quoridorserv <port>")
      return
    }

    var port: Int = 0

    try {
      port = args(0).toInt
      if (port < 1 || port > 65535) {
        throw new IllegalArgumentException
      }
    } catch {
      case e: NumberFormatException => {
        println("ERROR: Invalid port number: " + args(0))
        return
      }
      case e: IllegalArgumentException => {
        println("ERROR: Port number out of range [1-65535]: " + args(0))
        return
      }
    }
   
    try {
      val listener = new ConnectionListener(port)
      listener start
    } catch {
      case e: Throwable => {
        println("ERROR: Cannot start connection listener on port " + port + ": " + e.getMessage)
        return
      }
    }

    println (ABOUT)
    println("Listening on port " + port) 
    
  }

}