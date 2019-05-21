package cc.ayakurayuki.csa.starter.module

import cc.ayakurayuki.csa.starter.dao.ContentDAO
import cc.ayakurayuki.csa.starter.dao.SettingDAO
import com.google.inject.AbstractModule
import com.google.inject.Scopes

/**
 *
 * @author ayakurayuki* @date 2019/05/20-15:32
 */
class DAOModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ContentDAO.class).in(Scopes.SINGLETON)
    bind(SettingDAO.class).in(Scopes.SINGLETON)
  }

}
