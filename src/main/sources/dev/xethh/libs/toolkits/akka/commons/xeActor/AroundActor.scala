package dev.xethh.libs.toolkits.akka.commons.xeActor

import akka.actor.Actor

trait ActorMessage
abstract class AroundReceiveActor[AM <: ActorMessage] extends Actor {

  override def aroundReceive(receive: Receive, msg: Any): Unit = {
    if(msg.isInstanceOf[ActorMessage]){
      before(receive, msg.asInstanceOf[AM])
      super.aroundReceive(receive, msg)
      after(receive, msg.asInstanceOf[AM])
    }
    else
      super.aroundReceive(receive, msg)
  }

  def before(receive:Receive, msg:AM): Unit
  def after(receive:Receive, msg:AM): Unit
}
