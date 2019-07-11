package cc.ayakurayuki.csa.starter.verticle

import cc.ayakurayuki.csa.starter.api.ApiRest
import cc.ayakurayuki.csa.starter.core.auth.TokenValidator
import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.config.RestRouter
import cc.ayakurayuki.csa.starter.module.DAOModule
import cc.ayakurayuki.csa.starter.module.ServiceModule
import cc.ayakurayuki.csa.starter.pool.HikariCPManager
import cc.ayakurayuki.csa.starter.service.SettingService
import com.google.inject.Guice
import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

/**
 *
 * @author ayakurayuki* @date 2019/05/20-14:51
 */
class StarterVerticle extends AbstractVerticle {

  private def logger = LoggerFactory.getLogger this.getClass()
  private HttpServer server
  private Router router

  private def start
  private def end

  @Override
  void start() throws Exception {
    start = System.currentTimeMillis()
    logger.info "Starting ${config().getString('product.name', 'csa')} server version ${config().getString('product.version', '')}"
    super.start()
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
    logger.info 'Loading context'
    Constants.init context
    logger.info 'Loaded context'
    Constants.injector = Guice.createInjector(
        new DAOModule(),
        new ServiceModule()
    )
    logger.info 'Loaded injector'
    HikariCPManager.init()
    logger.info 'Loaded CP manager'
//    EventBusManager.init()
  }

  private def router() {
    logger.info 'Loading router'
    def _router = Router.router(vertx)
    _router.route().handler(BodyHandler.create())
    _router.route().handler(authHandler)
    RestRouter.register(_router, ApiRest.class)
    this.router = _router

    def routedUriCount = this.router.routes.findAll({ r -> StringUtils.isNotEmpty(r.path) }).size()
    def routedNoneUriCount = this.router.routes.findAll({ r -> StringUtils.isEmpty(r.path) }).size()
    logger.info "Loaded $routedUriCount paths, $routedNoneUriCount handlers"
  }

  private def serve() {
    this.server = vertx.createHttpServer()
    def port = config().getInteger('server.port', 8888)
    this.server.requestHandler(this.router).listen(port, { r ->
      if (r.succeeded()) {
        end = System.currentTimeMillis()
        logger.info "Done (${(end - start) / 1000}s)! Server is now listening to port $port."
      } else {
        logger.error "Error! Server is going down cause ${r.cause().localizedMessage}"
      }
    })
  }

  private Handler<RoutingContext> authHandler = { context ->
    def authUrlMap = RestRouter.authUrlMap
    def request = context.request()
    if (!authUrlMap.containsKey(request.path())) {
      context.next()
      return
    }
    def token = request.getFormAttribute('token')
    if (token == null) {
      context.user = new TokenValidator(token, -400, 'Require token!')
      context.next()
    } else {
      Constants.injector.getInstance(SettingService.class)[Constants.TOKEN]
          .setHandler { ar ->
            if (ar.succeeded()) {
              if (ar.result() == null) {
                context.user = new TokenValidator(token, -500, 'Token expired.')
              } else {
                def storedToken = ar.result().value
                if (StringUtils.isEmpty(storedToken)) {
                  context.user = new TokenValidator(token, -500, 'Token expired.')
                } else if (storedToken != token) {
                  context.user = new TokenValidator(token, -400, 'Wrong token, access denied!')
                } else {
                  context.user = new TokenValidator(token, 0, '')
                }
              }
            } else {
              context.user = new TokenValidator(
                  token, -500, "Found other exception that I cannot handle with cause: ${ar.cause().localizedMessage}"
              )
            }
            context.next()
          }
    }
  }

}
