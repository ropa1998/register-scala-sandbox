package com.example.register.server

import akka.actor.ActorRef
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, path, post, _}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.example.register.actor.RegisterActor
import com.example.register.actor.RegisterActor.{GetStats, Measurement, SearchResponse}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

class RegisterRoutes(registerRepositoryActor: ActorRef)(implicit val ec: ExecutionContext, implicit val timeout: Timeout) extends JsonSupport {

  def storeRegisterHandler(measurement: Measurement): Route = {
    handleRepositoryResponse(askRepository(measurement)) { _ =>
      complete(HttpResponse(StatusCodes.Accepted))
    }
  }

  def searchRegisterHandler(req: GetStats): Route = {
    handleRepositoryResponse(askRepository(req).mapTo[Option[SearchResponse]]) {
      case Some(todo) => complete(todo)
      case None => complete(HttpResponse(StatusCodes.NotFound))
    }
  }

  private def askRepository(m: RegisterActor.Message) = registerRepositoryActor ? m

  private def handleRepositoryResponse[A](f: Future[A])(success: A => Route) = {
    onComplete(f) {
      case Success(v) => success(v)
      case _ => complete(HttpResponse(StatusCodes.InternalServerError))
    }
  }

  def make: Route = path("register") {
    post {
      entity(as[Measurement]) { todo =>
        storeRegisterHandler(todo)
      }
    } ~ post {
      entity(as[GetStats]) { req =>
        searchRegisterHandler(req)
      }
    }
  }


}
