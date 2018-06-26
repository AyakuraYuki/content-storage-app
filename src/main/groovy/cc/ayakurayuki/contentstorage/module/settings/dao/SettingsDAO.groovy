package cc.ayakurayuki.contentstorage.module.settings.dao

import cc.ayakurayuki.contentstorage.module.settings.entity.Settings
import org.springframework.stereotype.Repository

/**
 * @author ayakurayuki
 * @date 2017/10/19
 */
@Repository("SettingsDAO")
interface SettingsDAO {

  Settings get(String id)

  List<Settings> list()

  List<Settings> search(Settings t)

  int insert(Settings t)

  int update(Settings t)

  int delete(Settings t)

}
