package cc.ayakurayuki.contentstorage.dao

import cc.ayakurayuki.contentstorage.entity.Settings
import org.springframework.stereotype.Repository

/**
 * Created by Ayakura Yuki on 2017/10/19.
 */
@Repository("SettingsDao")
interface SettingsDao {

    Settings get(String id)

    List<Settings> list()

    List<Settings> search(Settings settings)

    int insert(Settings settings)

    int update(Settings settings)

    int delete(Settings settings)

}
