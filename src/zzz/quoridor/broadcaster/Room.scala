package zzz.quoridor.broadcaster

import scala.collection.mutable.ListBuffer
import java.net.Socket
import scala.collection.mutable.Map

class Room(val name: String) {

  val order = List("BLUE", "RED", "GREEN", "YELLOW")

  val colors = Map[String, Boolean]()
  colors += ("BLUE" -> false)
  colors += ("RED" -> false)
  colors += ("GREEN" -> false)
  colors += ("YELLOW" -> false)

  val sockets = new ListBuffer[Socket]

  def retainColor: Option[String] = {
    order foreach (key => {
      if (!colors.get(key).get) {
        colors.update(key, true)
        return Some(key)
      }
    })
    return None
  }

  def releaseColor(color: String) {
    colors.update(color, false)
  }

}