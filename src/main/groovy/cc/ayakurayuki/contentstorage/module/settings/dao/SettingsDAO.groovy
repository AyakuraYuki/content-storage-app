package cc.ayakurayuki.contentstorage.module.settings.dao

import cc.ayakurayuki.contentstorage.common.base.BaseDAO
import cc.ayakurayuki.contentstorage.module.settings.entity.Settings
import org.springframework.stereotype.Repository

/**
 * Created by Ayakura Yuki on 2017/10/19.
 */
@Repository("SettingsDAO")
interface SettingsDAO extends BaseDAO<Settings> {
}
