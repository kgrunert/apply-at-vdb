package nl.vandebron.applytovdb

import cats.effect.{ContextShift, ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder
import org.slf4j.LoggerFactory
import pureconfig.ConfigSource
import org.http4s.implicits._
import pureconfig.generic.auto._

import scala.io.StdIn

case class RequestBody(
    email: String,
    name: String,
    phoneNumber: String,
    applicationBase64: String,
    referredBy: Option[String]
)

case class Config(host: String, port: Int)
object Main extends IOApp {

  implicit val ec = scala.concurrent.ExecutionContext.global
  implicit val cs: ContextShift[IO] =
    IO.contextShift(ec)
  val ioConcurrentEffect = IO.ioConcurrentEffect(cs)

  val logger = LoggerFactory.getLogger(this.getClass())

  val program = for {
    config <- IO(ConfigSource.default.loadOrThrow[Config]) // load config here
    service = new ApplicationServiceImpl()
    server = BlazeServerBuilder[IO](ioConcurrentEffect, timer)
      .bindHttp(config.port, config.host)
      .withHttpApp(new Routes(service).httpApp)
    fiber =
      server.resource
        .use { _ =>
          logger.info(
            s"Serving at ${config.host}:${config.port}.\nPress enter to exit..."
          )
          IO(StdIn.readLine())
        }
        .as(ExitCode.Success)
  } yield fiber

  def run(args: List[String]): IO[ExitCode] =
    program.attempt.unsafeRunSync match {
      case Left(e) =>
        IO {
          println("*** An error occured! ***")
          if (e != null) {
            println(e.getMessage())
          }
          ExitCode.Error
        }
      case Right(r) => r
    }

}
