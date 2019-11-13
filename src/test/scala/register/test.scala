

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.example.register.actor.RegisterActor
import com.example.register.actor.RegisterActor.{GetStats, Measurement, SearchResponse}
import com.example.register.repository.MapRepository
import org.scalatest.{AsyncWordSpecLike, BeforeAndAfterAll, Matchers}

class test extends TestKit(ActorSystem("MySpec"))
  with DefaultTimeout
  with ImplicitSender
  with AsyncWordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  val repository: ActorRef = system.actorOf(RegisterActor.props(new MapRepository()))

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "TestRepositoryActor" should {

    "check an empty register" in {
      val x = repository ? GetStats("Rodrigo")
      x.mapTo[SearchResponse].map(_.max shouldBe (0))
      x.mapTo[SearchResponse].map(_.min shouldBe (0))
      x.mapTo[SearchResponse].map(_.total shouldBe Nil)
      x.mapTo[SearchResponse].map(_.count shouldBe (0))
    }

    "check for a registry with one register" in {
      repository ! Measurement("Rodrigo", 23)
      val x = repository ? GetStats("Rodrigo")
      x.mapTo[SearchResponse].map(_.max shouldBe (23))
      x.mapTo[SearchResponse].map(_.min shouldBe (23))
      x.mapTo[SearchResponse].map(_.total shouldBe List(23))
      x.mapTo[SearchResponse].map(_.count shouldBe (1))
    }

    "check for a registry with two registers" in {
      repository ! Measurement("Rodrigo", 48)
      val x = repository ? GetStats("Rodrigo")
      x.mapTo[SearchResponse].map(_.max shouldBe (48))
      x.mapTo[SearchResponse].map(_.min shouldBe (23))
      x.mapTo[SearchResponse].map(_.total shouldBe List(23, 48))
      x.mapTo[SearchResponse].map(_.count shouldBe (2))
    }


  }
}
