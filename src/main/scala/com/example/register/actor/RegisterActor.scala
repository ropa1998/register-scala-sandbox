package com.example.register.actor

import akka.actor.{Actor, Props}
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import com.example.register.actor.RegisterActor.{GetStats, Measurement}
import com.example.register.traits.RegisterRepository


object RegisterActor {

  trait Message

  case class Measurement(name: String, value: Double) extends Message

  case class GetStats(name: String) extends Message

  case class SearchResponse(min: Double, max: Double, total: List[Double], count: Int)

  def props(repository: RegisterRepository): Props = Props(new RegisterActor(repository))

}


class RegisterActor(repository: RegisterRepository) extends Actor {

  override def receive: Receive = {
    case Measurement(name, value) => sender() ! repository.save(name, value)
    case GetStats(name) => sender() ! repository.search(name)
  }
}
