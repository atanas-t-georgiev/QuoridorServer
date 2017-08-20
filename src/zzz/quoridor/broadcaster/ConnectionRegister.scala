package zzz.quoridor.broadcaster

import java.net.Socket
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap

object ConnectionRegister {

  val register = new HashMap[String, Room]()

  def +=(socket: Socket, room: String): Room = {
    var r: Room = null
    this.synchronized {
      r = register.get(room).getOrElse(null)
      if (r == null) {
        System.out.println("Room created [" + room + "]")
        r = new Room(room)
        register.put(room, r)
      }
    }
    r.synchronized {
      println("Connection registered in room [" + room + "]")
      r.sockets += socket
    }
    return r
  }

  def -=(socket: Socket, room: String) {
    var r: Room = null
    this.synchronized {
      r = register.get(room).getOrElse(null)
    }
    if (r != null) {
      r.synchronized {
        println("Connection un-registered from room [" + room + "]")
        r.sockets -= socket
      }
      this.synchronized {
        if (r.sockets.isEmpty) {
          println("Room [" + room + "] closed")
          register.remove(r.name)
        }
      }
    } else {
      println("ERROR: Trying to remove nonexistent room")
    }

  }

  def #=(message: String, room: String) {
    var r: Room = null
    this.synchronized {
      r = register.get(room).getOrElse(null)
    }
    if (r != null) {
      r.synchronized {
        println("Broadcasting message [" + room + "]: " + new String(message))
        r.sockets.foreach(connection => {
          try {
            connection.getOutputStream write ((message + "\n") getBytes)
            connection.getOutputStream flush
          } catch {
            case _: Throwable =>
          }
        })
      }
    } else {
      println("ERROR: Trying to broadcast in nonexistent room")
    }
  }

}