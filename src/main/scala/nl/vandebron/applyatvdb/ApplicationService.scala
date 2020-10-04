package nl.vandebron.applytovdb

import java.nio.charset.StandardCharsets
import java.{util => ju}

import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}

trait ApplicationService {

  def uploadApplicationToS3(
      application: RequestBody
  ): Future[Either[String, String]]
}

class ApplicationServiceImpl()(implicit ec: ExecutionContext)
    extends ApplicationService {

  val logger = LoggerFactory.getLogger(this.getClass)

  override def uploadApplicationToS3(
      application: RequestBody
  ): Future[Either[String, String]] =
    Future.successful {
      logger.info(s"Received ${application}")
      val file = new String(
        ju.Base64.getDecoder.decode(application.applicationBase64),
        StandardCharsets.UTF_8
      )
      logger.info(s"Decoded application: $file")
      Right(
        "*confetti* Thanks for handing your application, we will get back to you within the next days! *confetti*"
      )
    }
}
