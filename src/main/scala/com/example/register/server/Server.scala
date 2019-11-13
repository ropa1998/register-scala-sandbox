package com.example.register.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.example.register.actor.RegisterActor
import com.example.register.repository.MapRepository

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.io.StdIn

object Server extends App {

  implicit private val system: ActorSystem = ActorSystem("actorSystem")
  implicit private val materializer: ActorMaterializer = ActorMaterializer()
  implicit private val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit private val timeout: Timeout = Timeout(30 seconds)

  private val todoRepository = new MapRepository()
  private val todoRepositoryActor = system.actorOf(RegisterActor.props(todoRepository), "todoRepositoryActor")
  private val todoRoutes = new RegisterRoutes(todoRepositoryActor).make
  private val bindingFuture = Http().bindAndHandle(todoRoutes, "localhost", 8080)

  println("Server online at http://localhost:8080\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())


}
