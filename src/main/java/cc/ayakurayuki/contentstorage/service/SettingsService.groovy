package cc.ayakurayuki.contentstorage.service

import cc.ayakurayuki.contentstorage.dao.SettingsDao
import cc.ayakurayuki.contentstorage.entity.Settings
import cc.ayakurayuki.contentstorage.utils.GoogleAuthenticator
import cc.ayakurayuki.contentstorage.utils.IDUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Ayakura Yuki on 2017/10/19.
 */
@Service("SettingsService")
class SettingsService {

    @Autowired
    SettingsDao settingsDao

    Settings get(String id) {
        settingsDao.get(id)
    }

    List<Settings> list() {
        settingsDao.list()
    }

    List<Settings> search(Settings settings) {
        settingsDao.search(settings)
    }

    int insert(Settings settings) {
        settingsDao.insert(settings)
    }

    int update(Settings settings) {
        settingsDao.update(settings)
    }

    int delete(Settings settings) {
        settingsDao.delete(settings)
    }

    Settings getSecretSetting() {
        Settings forSearching = new Settings()
        forSearching.key = "secret"
        List<Settings> list = settingsDao.search(forSearching)
        list.size() == 0 ? null : list.get(0)
    }

    String getSecretKeyFromDatabase() {
        Settings settings = getSecretSetting()
        if (settings == null) {
            settings = new Settings()
            settings.id = IDUtils.UUID()
            settings.key = "secret"
            settings.value = GoogleAuthenticator.generateSecretKey()
            insert(settings)
        }
        settings.value = GoogleAuthenticator.generateSecretKey()
        update(settings)
        return settings.value
    }

}
