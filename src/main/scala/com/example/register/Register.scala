package com.example.register

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import com.example.register.actor.RegisterActor
import com.example.register.actor.RegisterActor.{GetStats, Measurement, SearchResponse}
import com.example.register.repository.MapRepository

import scala.concurrent.{Await, ExecutionContextExecutor}

object Register extends App {
//  val mapRepository = new MapRepository()
//  implicit private val system: ActorSystem = ActorSystem("actorSystem")
//  implicit private val timeout: Timeout = Timeout(30.seconds)
//  implicit private val executionContext: ExecutionContextExecutor = system.dispatcher
//
//
//  //#actor-system
//  val registerActor: ActorRef = system.actorOf(RegisterActor.props(mapRepository), "Register")
//  //#actor-system
//
//  //#main-send-messages
//  registerActor ! Meassurement("Rodrigo", 23)
//  //#main-send-messages
//  val sr = Await.result((registerActor ? GetStats("Rodrigo")).mapTo[SearchResponse], 30.seconds)
}
