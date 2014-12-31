package cchat

import java.net.InetSocketAddress
import scala.util.{Random, Try}

import akka.actor.ActorSystem

object Main {

  object Port {
    def unapply(text: String): Option[Int] = Try(text.toInt).toOption
  }

  def main(args: Array[String]): Unit = {
    args match {
      case Array("connect", host, Port(port)) => connect(new InetSocketAddress(host, port))
      case Array("serve", host, Port(port)) => serve(new InetSocketAddress(host, port))
      case _ => showUsage()
    }
  }

  private def showUsage(): Unit = {
    println(
      """Usage: connect <server_host> <server_port>
        |       serve <listen_host> <listen_port>
      """.stripMargin)
    System.exit(0)
  }

  private def connect(endpoint: InetSocketAddress): Unit = {
    val id = Random.nextInt(10000).toString
    println(s"Connecting to $endpoint with id $id")
    val system = ActorSystem("client")
    val client = system.actorOf(ChatClientActor.props(id, endpoint))
    new Shell(client).run()
    client ! ChatClientActor.CloseChat
    system.shutdown()
    system.awaitTermination()
  }

  private def serve(endpoint: InetSocketAddress): Unit = {
    println(s"Listening as $endpoint")
    val system = ActorSystem("server")
    system.actorOf(ChatServerActor.props(endpoint))
    system.awaitTermination()
  }
}
