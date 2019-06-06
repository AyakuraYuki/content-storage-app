package cc.ayakurayuki.csa.starter.mq

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.service.SettingService
import io.vertx.core.Future
import io.vertx.core.eventbus.EventBus
import org.slf4j.LoggerFactory

class EventBusManager {

  private static final def logger = LoggerFactory.getLogger EventBusManager.class

  enum MQAddress {
    DeleteToken('cc.ayakurayuki.csa.system.command.deleteToken')

    private final String address

    MQAddress(String address) {
      this.address = address
    }

    String getAddress() {
      return address
    }
  }

  private static def deleteToken(EventBus eb) {
    eb.consumer MQAddress.DeleteToken.address, { message ->
      Constants.injector.getInstance(SettingService.class).delete(Constants.TOKEN)
          .compose { Void ->
            logger.info 'Message consumed, token has been deleted.'
            Future.succeededFuture()
          }
    }
  }

  static def init() {
    def eb = Constants.eventBus
    if (eb == null) {
      logger.info 'Missing EventBus, cannot bind consumer handlers.'
    }
    deleteToken eb // :-)
  }

}
