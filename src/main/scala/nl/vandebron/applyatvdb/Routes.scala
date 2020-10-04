package nl.vandebron.applytovdb

import cats.effect.{ContextShift, IO}
import io.circe.generic.auto._
import org.http4s.implicits._
import org.http4s.server.Router
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s._

class Routes(applicationService: ApplicationService)(implicit
    cs: ContextShift[IO]
) {

  private def getHelloWorld(): Either[String, String] = Right("Hello World!")

  private val postApplication
      : ServerEndpoint[RequestBody, String, String, Nothing, IO] =
    endpoint.post
      .description("Example endpoint for posting the application")
      .in("apply")
      .in(jsonBody[RequestBody])
      .errorOut(jsonBody[String])
      .out(jsonBody[String])
      .serverLogic { input =>
        IO.fromFuture(IO(applicationService.uploadApplicationToS3(input)))
      }

  val routes = postApplication.toRoutes

  val httpApp =
    Router(
      "/api/v1" -> routes
    ).orNotFound
}
