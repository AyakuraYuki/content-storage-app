package cc.ayakurayuki.csa.starter.module

import cc.ayakurayuki.csa.starter.service.ContentService
import cc.ayakurayuki.csa.starter.service.SettingService
import com.google.inject.AbstractModule
import com.google.inject.Scopes

/**
 *
 * @author ayakurayuki* @date 2019/05/20-15:31
 */
class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SettingService.class).in(Scopes.SINGLETON)
    bind(ContentService.class).in(Scopes.SINGLETON)
  }

}
