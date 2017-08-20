package zzz.quoridor.broadcaster

import scala.util.control.Breaks._
import java.net.ServerSocket

class ConnectionListener(val port: Int) extends Thread {

  val serverSocket = new ServerSocket(port)

  override def run {

    breakable {

      while (!Thread.interrupted) {

        try {
          new DataReader(serverSocket.accept).start
        } catch {
          case _: Throwable => {
            println("ERROR: Server accept interrupted. Stopping listener.")
            break
          }

        }

      }

    }

    try {
      serverSocket close
    } catch {
      case _: Throwable =>
    }

  }

}