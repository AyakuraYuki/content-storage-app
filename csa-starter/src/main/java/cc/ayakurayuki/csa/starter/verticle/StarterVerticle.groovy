package cc.ayakurayuki.csa.starter.verticle

import cc.ayakurayuki.csa.starter.api.ApiRest
import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.module.DAOModule
import cc.ayakurayuki.csa.starter.module.ServiceModule
import cc.ayakurayuki.csa.starter.pool.HikariCPManager
import com.google.inject.Guice
import com.zandero.rest.RestRouter
import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.slf4j.LoggerFactory

/**
 *
 * @author ayakurayuki* @date 2019/05/20-14:51
 */
class StarterVerticle extends AbstractVerticle {

  private def logger = LoggerFactory.getLogger(this.getClass())
  private HttpServer server
  private Router router

  private def start
  private def end

  @Override
  void start() throws Exception {
    super.start()
    start = System.currentTimeMillis()
    components()
    router()
    serve()
  }

  @Override
  void stop() throws Exception {
    super.stop()
    HikariCPManager.close()
    server?.close({ r ->
      logger.info "Server ${r.succeeded() ? 'closed.' : "still running cause ${r.cause().localizedMessage}"}"
    })
  }

  private def components() {
    Constants.init context
    Constants.injector = Guice.createInjector(
        new DAOModule(),
        new ServiceModule()
    )
    HikariCPManager.init()
  }

  private def router() {
    def _router = Router.router(vertx)
    _router.route().handler(BodyHandler.create())
    RestRouter.register(_router, ApiRest.class)
    this.router = _router
  }

  private def serve() {
    this.server = vertx.createHttpServer()
    def port = config().getInteger('server.port', 8888)
    this.server.requestHandler(this.router).listen(port, { r ->
      if (r.succeeded()) {
        end = System.currentTimeMillis()
        logger.info "Done <${(end - start) / 1000}s>! Server is now listening port $port."
      } else {
        logger.error "Error! Server is going down cause ${r.cause().localizedMessage}"
      }
    })
  }

}
