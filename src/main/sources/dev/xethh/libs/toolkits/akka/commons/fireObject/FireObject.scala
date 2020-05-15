package dev.xethh.libs.toolkits.akka.commons.fireObject

import java.util.Date

import me.xethh.utils.dateManipulation.DateFactory

import scala.concurrent.duration.Duration

/**
 * Fire object controlling the fire or trigger of signals for some input in specific period of time
 */
trait FireObject[S]{
  def shouldFire(signal:S) : Option[S]
}

class FirstFireObject[S](duration: Duration) extends FireObject[S] {
  var lastTrigger:Option[(Date, S)] = None

  override def shouldFire(signal: S): Option[S] = {
    if(lastTrigger.isEmpty) {
      lastTrigger=Some((new Date, signal))
      Some(lastTrigger.get._2)
    } else{
      if(DateFactory.from(lastTrigger.get._1).addMS(duration.toMillis.intValue()).before(DateFactory.now())){
        lastTrigger=Some(new Date, signal)
        Some(lastTrigger.get._2)
      }
      else
        None
    }
  }
}

class BaseFireObject[S]() extends FireObject[S]{
  var signal:Option[S] = None

  override def shouldFire(signal: S): Option[S] = {
    if(this.signal.isEmpty) {
      this.signal = Some(signal)
      None
    }
    else this.signal
  }
}
