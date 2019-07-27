package cc.ayakurayuki.csa.starter.core.exception.handler

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.JsonResponse
import com.zandero.rest.exception.ExceptionHandler
import com.zandero.utils.StringUtils
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 *
 * @author ayakurayuki* @date 2019/07/15-10:25
 */
class GenericExceptionHandler implements ExceptionHandler<Throwable> {

  @Override
  void write(Throwable throwable, HttpServerRequest request, HttpServerResponse response) throws Throwable {
    def message = throwable.message
    if (StringUtils.isNullOrEmptyTrimmed(message)) {
      message = throwable.toString()
    }
    response.putHeader 'content-type', MediaType.APPLICATION_JSON
    response.chunked = true
    response.setStatusCode Response.Status.OK.statusCode
    def json = new JsonResponse(Constants.ErrorCode.OTHERS.code, message, null)
    response.end Json.encodePrettily(json)
  }

}
