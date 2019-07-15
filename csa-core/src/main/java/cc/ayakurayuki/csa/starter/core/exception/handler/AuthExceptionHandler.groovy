package cc.ayakurayuki.csa.starter.core.exception.handler

import cc.ayakurayuki.csa.starter.core.entity.JsonResponse
import cc.ayakurayuki.csa.starter.core.exception.AuthException
import com.zandero.rest.exception.ExceptionHandler
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 *
 * @author ayakurayuki* @date 2019/06/06-15:21
 */
class AuthExceptionHandler implements ExceptionHandler<AuthException> {

  @Override
  void write(AuthException result, HttpServerRequest request, HttpServerResponse response) throws Throwable {
    response.putHeader 'content-type', MediaType.APPLICATION_JSON
    response.chunked = true
    response.statusCode = Response.Status.OK.statusCode
    def json = new JsonResponse(result.status, result.message, null)
    response.end Json.encodePrettily(json)
  }

}
