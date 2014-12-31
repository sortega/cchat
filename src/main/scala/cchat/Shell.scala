package cchat

import java.io.PrintWriter

import scala.annotation.tailrec

import akka.actor.ActorRef
import jline.console.ConsoleReader

class Shell(client: ActorRef) {
  import Shell._

  private val console = new ConsoleReader(System.in, System.out)
  private val formatter = new PrintWriter(console.getOutput)

  def run(): Unit = try {
    interpreterLoop()
  } finally {
    console.shutdown()
  }


  @tailrec
  private def interpreterLoop(): Unit = {
    console.readLine(">> ") match {
      case null | ChatPattern("exit", _) => // Exit loop

      case EmptyLinePattern() => interpreterLoop()

      case ChatPattern(target, message) =>
        client ! ChatClientActor.SendLine(target, message)
        interpreterLoop()
    }
  }
}

object Shell {
  private val ChatPattern = """\s*(\S+)\s*(.*?)\s*""".r
  private val EmptyLinePattern = """\s*""".r
}
