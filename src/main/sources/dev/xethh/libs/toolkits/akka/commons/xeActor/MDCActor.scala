package dev.xethh.libs.toolkits.akka.commons.xeActor

import java.util

import me.xethh.libs.toolkits.threadLocal.{XEContext, XEContextRaw}
import org.slf4j.MDC

trait MDCActorMessage extends ActorMessage {
  def map():java.util.Map[String,Object]
}
class MDCActorMessageAbs() extends MDCActorMessage {
  val privateMap:java.util.Map[String, Object] = XEContextRaw.copy().asInstanceOf[java.util.Map[String, Object]]
  override def map(): util.Map[String, Object] = privateMap
}
abstract class MDCAroundReceiveActor[AM <: MDCActorMessageAbs] extends AroundReceiveActor[AM] {
  def before(receive:Receive, msg:AM): Unit={
    MDC.clear()
    msg.map().entrySet().forEach{
      entry=> XEContextRaw.put(entry.getKey,entry.getValue)
    }
  }
  def after(receive:Receive, msg:AM): Unit={
    XEContextRaw.copy().entrySet().forEach(x=>XEContextRaw.remove(x.getKey))
  }
}
