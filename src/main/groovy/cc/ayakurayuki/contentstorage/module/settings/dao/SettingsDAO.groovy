package cc.ayakurayuki.contentstorage.module.settings.dao

import cc.ayakurayuki.contentstorage.module.settings.entity.Settings
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * @author ayakurayuki
 * @date 2017/10/19
 */
@Repository('SettingsDAO')
interface SettingsDAO {

  Settings get(String id)

  Settings getByKey(@Param('key') String key)

  int insert(Settings t)

  int update(Settings t)

  int delete(Settings t)

}
