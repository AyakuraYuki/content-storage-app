package cc.ayakurayuki.csa.starter.core.exception.handler

import cc.ayakurayuki.csa.starter.core.entity.JsonResponse
import cc.ayakurayuki.csa.starter.core.exception.StatusCodeException
import com.zandero.rest.exception.ExceptionHandler
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 *
 * @author ayakurayuki* @date 2019/07/15-10:36
 */
class StatusCodeExceptionHandler implements ExceptionHandler<StatusCodeException> {

  @Override
  void write(StatusCodeException exception, HttpServerRequest request, HttpServerResponse response) throws Throwable {
    response.putHeader 'content-type', MediaType.APPLICATION_JSON
    response.putHeader 'Access-Control-Allow-Origin', '*'
    response.putHeader 'X-Application-Context', 'application'
    response.setStatusCode Response.Status.OK.statusCode
    response.chunked = true
    def json = new JsonResponse(exception.status, exception.message, null);
    response.end Json.encodePrettily(json)
  }

}
