package cc.ayakurayuki.csa.starter.module

import cc.ayakurayuki.csa.starter.dao.ContentDao
import cc.ayakurayuki.csa.starter.dao.SettingDao
import com.google.inject.AbstractModule
import com.google.inject.Scopes

/**
 *
 * @author ayakurayuki* @date 2019/05/20-15:32
 */
class DaoModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ContentDao.class).in(Scopes.SINGLETON)
    bind(SettingDao.class).in(Scopes.SINGLETON)
  }

}
