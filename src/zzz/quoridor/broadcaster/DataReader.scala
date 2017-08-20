package zzz.quoridor.broadcaster

import scala.util.control.Breaks._
import java.net.Socket
import java.io.IOException
import java.io.InputStreamReader
import java.io.BufferedReader

class DataReader(val socket: Socket) extends Thread {

  override def run {

    var roomName: String = null
    var room: Room = null
    var color: Option[String] = None
    
    try {

      val reader = new BufferedReader(new InputStreamReader(socket.getInputStream))

      val roomLine = reader.readLine
      val tokens = roomLine.split(" ")
      if (tokens.length != 2 || tokens(0) != "ROOM") {
        throw new Exception("Protocol violation (ROOM)")
      } else {
        roomName = tokens(1)
        room = ConnectionRegister += (socket, roomName)
      }

      color = room retainColor
      
      if (color.isDefined) {
        socket.getOutputStream write(("ACCEPTED " + color.get + "\n").getBytes)
        socket.getOutputStream.flush
      } else {
        socket.getOutputStream write(("REJECTED\n").getBytes)
        socket.getOutputStream.flush
        throw new Exception("Full room")
      }
      
      while (true) {

        val line = reader.readLine
        if (line == null) {
          throw new IOException("End of stream")
        }

        ConnectionRegister #= (line, roomName)

      }

    } catch {
      case e: Throwable => {
        println("Closing connection because of: " + e.getMessage);
        ConnectionRegister -= (socket, roomName)
        if (color.isDefined) {
          room.releaseColor(color.get)
        }
        try {
          socket close
        } catch {
          case ex: Throwable =>
        }
      }
    }

  }

}