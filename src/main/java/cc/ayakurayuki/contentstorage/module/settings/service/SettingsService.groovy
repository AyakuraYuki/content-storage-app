package cc.ayakurayuki.contentstorage.module.settings.service

import cc.ayakurayuki.contentstorage.common.util.GoogleAuthenticator
import cc.ayakurayuki.contentstorage.common.util.IDUtils
import cc.ayakurayuki.contentstorage.module.settings.dao.SettingsDAO
import cc.ayakurayuki.contentstorage.module.settings.entity.Settings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Ayakura Yuki on 2017/10/19.
 */
@Service("SettingsService")
@Transactional(readOnly = true)
class SettingsService {

    @Autowired
    SettingsDAO dao

    Settings get(String id) {
        dao.get(id)
    }

    List<Settings> list() {
        dao.list()
    }

    List<Settings> search(Settings settings) {
        dao.search(settings)
    }

    int insert(Settings settings) {
        dao.insert(settings)
    }

    int update(Settings settings) {
        dao.update(settings)
    }

    int delete(Settings settings) {
        dao.delete(settings)
    }

    Settings getSecretSetting() {
        Settings forSearching = new Settings()
        forSearching.key = "secret"
        def list = dao.search(forSearching)
        list.size() == 0 ? null : list.get(0)
    }

    String getSecretKeyFromDatabase() {
        def settings = secretSetting
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
