package cchat

import java.net.InetSocketAddress

import akka.actor._
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import cchat.ChatClientActor.{CloseChat, SendLine}

class ChatClientActor(id: String, endpoint: InetSocketAddress) extends Actor {

  import context.system

  IO(Tcp) ! Connect(endpoint)

  override def receive: Receive = {
    case CommandFailed(_: Connect) =>
      println("Cannot connect")
      system.shutdown()

    case c @ Connected(remote, local) => becomeConnected(sender())
  }

  private def becomeConnected(connection: ActorRef): Unit = {
    println("Connected")
    connection ! Register(self)
    context.become {
      case SendLine(target, message) => connection ! Write(ByteString(s"$id#$target#$message"))

      case CommandFailed(_: Write) => println("\nFailed to write!")

      case Received(data) =>
        data.utf8String.split("#") match {
          case Array(source, `id`, message) => println(s"\n$source> $message")
          case Array(source, other, message) => // Not for me
          case _ => println(s"\nUnexpected message: ${data.utf8String}")
        }

      case _: ConnectionClosed =>
        println("\nConnection closed")
        system.shutdown()

      case CloseChat => connection ! Close
    }
  }
}

object ChatClientActor {
  case class SendLine(target: String, message: String)
  case object CloseChat

  def props(id: String, endpoint: InetSocketAddress) = Props(new ChatClientActor(id, endpoint))
}
