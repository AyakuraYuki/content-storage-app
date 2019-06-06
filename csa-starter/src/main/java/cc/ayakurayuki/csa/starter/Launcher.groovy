package cc.ayakurayuki.csa.starter

import cc.ayakurayuki.csa.starter.verticle.StarterVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.buffer.Buffer
import org.slf4j.LoggerFactory

/**
 * Launcher
 * @author ayakurayuki
 */
class Launcher {

  private static final def logger = LoggerFactory.getLogger Launcher.class

  static void main(args) {
    def start = System.currentTimeMillis()
    def vertxOptions = new VertxOptions()
    vertxOptions.eventLoopPoolSize = 16
    def vertx = Vertx.vertx vertxOptions
    def deploymentOptions = new DeploymentOptions()
    deploymentOptions.workerPoolSize = 10
    deploymentOptions.maxWorkerExecuteTime = 10000
    deploymentOptions.instances = 1
    def inputStream = ClassLoader.getSystemResourceAsStream 'config.json'
    def configContent = inputStream.readLines().join(System.lineSeparator())
    def config = Buffer.buffer(configContent).toJsonObject()
    deploymentOptions.config = config
    vertx.deployVerticle StarterVerticle.class.name, deploymentOptions, { handler ->
      if (handler.succeeded()) {
        def end = System.currentTimeMillis()
        logger.info "Prepared <${(end - start) / 1000}s>! Launch verticles."
      } else {
        logger.error "Error! Server close.", handler.cause()
      }
    }
  }

}
