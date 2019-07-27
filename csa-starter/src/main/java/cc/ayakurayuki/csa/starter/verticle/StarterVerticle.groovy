package cc.ayakurayuki.csa.starter.verticle

import cc.ayakurayuki.csa.starter.api.ApiRest
import cc.ayakurayuki.csa.starter.api.ContentRest
import cc.ayakurayuki.csa.starter.core.auth.TokenAuthor
import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.config.RestRouter
import cc.ayakurayuki.csa.starter.core.entity.Setting
import cc.ayakurayuki.csa.starter.core.util.IdUtils
import cc.ayakurayuki.csa.starter.module.DaoModule
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

    final def productName = config().getString('product.name', 'csa')
    final def productVersion = config().getString('product.version', '')
    logger.info "Starting $productName server version $productVersion"

    super.start()
    components()
    router()
    application()
    serve()
  }

  @Override
  void stop() throws Exception {
    super.stop()
    HikariCPManager.close()
    server?.close({ r ->
      if (r.succeeded()) {
        logger.info 'Server closed.'
      } else {
        logger.info "Cause [${r.cause().localizedMessage}], Server is now going down."
      }
    })
  }


  private def components() {
    Constants.init context
    logger.info 'Loaded context'
    // Inject beans
    Constants.injector = Guice.createInjector(
        new DaoModule(),
        new ServiceModule()
    )
    logger.info 'Loaded injector'
    HikariCPManager.init()
    logger.info 'Loaded CP manager'
  }

  private def router() {
    logger.info 'Loading router'
    def _router = Router.router(vertx)
    _router.route().handler(BodyHandler.create())
    _router.route().handler(authHandler)
    RestRouter.register(_router, ApiRest.class)
    RestRouter.register(_router, ContentRest.class)
    this.router = _router

    def routedUriCount = this.router.routes.findAll({ r -> StringUtils.isNotEmpty(r.path) }).size()
    def routedNoneUriCount = this.router.routes.findAll({ r -> StringUtils.isEmpty(r.path) }).size()
    logger.info "Loaded $routedUriCount paths, $routedNoneUriCount handlers"
  }

  private def application() {
    // Generate DES key for the first time starting the server ...
    def service = Constants.injector.getInstance SettingService.class
    service[Constants.DES_KEY].setHandler { ar ->
      if (ar.succeeded() && ar.result() == null) {
        def desKey = [
            id   : IdUtils.UUID(),
            key  : Constants.DES_KEY,
            value: "${IdUtils.UUID()}${IdUtils.UUID()}".toString()
        ] as Setting
        service.save desKey
        logger.info 'Generated DES key'
      }
    }
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
      // Failed: require for token but not found
      context.user = new TokenAuthor(token, Constants.ErrorCode.AUTH_FAILED.code, 'Require for token.')
      context.next()
    } else {
      final def service = Constants.injector.getInstance SettingService.class

      service[Constants.TOKEN_EXPIRE_TIME].setHandler { ex ->
        if (ex.succeeded()) {
          if (ex.result() != null) {
            def expire = ex.result().value.toLong()
            if (System.currentTimeMillis() <= expire) {

              // => Keep going: the token isn't expire
              service[Constants.TOKEN].setHandler { tk ->
                if (tk.succeeded()) {
                  if (tk.result() != null) {
                    final def storedToken = tk.result().value
                    if (StringUtils.isEmpty(storedToken)) {
                      // - Failed: empty token
                      context.user = new TokenAuthor(token, Constants.ErrorCode.TOKEN_EXPIRED.code, 'Token expired!')
                    } else if (storedToken != token) {
                      // - Failed: wrong token, token from the request isn't equal with the stored token
                      context.user = new TokenAuthor(token, Constants.ErrorCode.AUTH_FAILED.code, 'Access denied! Where did you get this token?')
                    } else {
                      // + Succeed: access confirmed
                      context.user = new TokenAuthor(token, 0, '')
                    }
                  } else {
                    // - Failed: missing token
                    context.user = new TokenAuthor(token, Constants.ErrorCode.TOKEN_EXPIRED.code, 'Token expired!')
                  }
                } else {
                  // - Failed: unexpected exception
                  context.user = new TokenAuthor(token, Constants.ErrorCode.OTHERS.code, "Unexpected exception!!! ${ex.cause().localizedMessage}")
                }
                context.next() // This works in the inner future.
              }
              return // When the inner future completed, it's no need to proceed.

            } else {
              // - Failed: token expired
              context.user = new TokenAuthor(token, Constants.ErrorCode.TOKEN_EXPIRED.code, 'Token expired!')
            }
          } else {
            // - Failed: token not found in server
            context.user = new TokenAuthor(token, Constants.ErrorCode.TOKEN_EXPIRED.code, 'Token expired!')
          }
        } else {
          // - Failed: unhandled exception
          context.user = new TokenAuthor(token, Constants.ErrorCode.OTHERS.code, "Found unhandled exception, cause: ${ex.cause().localizedMessage}")
        }
        context.next()
      }
    }
  }

}
