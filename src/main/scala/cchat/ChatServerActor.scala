package cchat

import java.net.InetSocketAddress

import akka.actor._
import akka.io.Tcp._
import akka.io.{IO, Tcp}

class ChatServerActor(endpoint: InetSocketAddress) extends Actor {
  import context.system

  private var connections = Map.empty[ActorRef, InetSocketAddress]

  IO(Tcp) ! Bind(self, endpoint)

  override def receive: Receive = {
    case Bound(localAddress) =>
      println(s"Server started at $localAddress")

    case CommandFailed(_: Bind) =>
      println("Cannot bind")
      system.shutdown()

    case Connected(remote, local) =>
      println(s"New connection from $remote")
      connections += sender -> remote
      sender ! Register(self)

    case Received(data) =>
      for (connection <- connections.keys if connection != sender) {
        connection ! Write(data)
      }

    case PeerClosed if connections.contains(sender()) =>
      println(s"Connection from ${connections(sender())} closed")
  }
}

object ChatServerActor {
  def props(endpoint: InetSocketAddress) = Props(new ChatServerActor(endpoint))
}
