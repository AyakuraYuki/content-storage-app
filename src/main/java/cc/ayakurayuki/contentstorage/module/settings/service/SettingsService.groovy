package cc.ayakurayuki.contentstorage.module.settings.service

import cc.ayakurayuki.contentstorage.common.base.BaseBean
import cc.ayakurayuki.contentstorage.common.util.GoogleAuthenticator
import cc.ayakurayuki.contentstorage.common.util.IDUtils
import cc.ayakurayuki.contentstorage.common.util.JsonMapper
import cc.ayakurayuki.contentstorage.common.util.RandomKeyUtils
import cc.ayakurayuki.contentstorage.module.settings.dao.SettingsDAO
import cc.ayakurayuki.contentstorage.module.settings.entity.EmergencyKey
import cc.ayakurayuki.contentstorage.module.settings.entity.Settings
import com.google.common.collect.Lists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Ayakura Yuki on 2017/10/19.
 */
@Service("SettingsService")
@Transactional(readOnly = true)
class SettingsService extends BaseBean {

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

  /**
   * 从数据库获取包含Google Authenticator特征码的Settings对象。
   * @return 数据库中存在的对象，不存在则返回null。
   */
  Settings getSecretSetting() {
    def by = new Settings()
    by.key = SECRET
    def list = dao.search(by)
    list.size() == 0 ? null : list.get(0)
  }

  /**
   * 获取特征码
   * @return 特征码字符串
   */
  String getSecretKeyFromDatabase() {
    def settings = secretSetting
    if (settings == null) {
      settings = new Settings()
      settings.id = IDUtils.UUID()
      settings.key = SECRET
      settings.value = GoogleAuthenticator.generateSecretKey()
      insert(settings)
    } else {
      settings.value = GoogleAuthenticator.generateSecretKey()
      update(settings)
    }
    return settings.value
  }

  /**
   * 获取应急码Settings对象
   * @return
   */
  Settings getEmergencySetting() {
    def by = new Settings()
    by.key = EMERGENCY
    def list = dao.search(by)
    list.size() == 0 ? null : list.get(0)
  }

  /**
   * 获取应急码
   * @return
   */
  List<EmergencyKey> generateEmergencyCode() {
    List<EmergencyKey> list = Lists.newArrayList()
    for (i in 1..10) {
      def key = new EmergencyKey()
      key.key = RandomKeyUtils.emergencyKey
      key.effective = true
      list.add(key)
    }
    def settings = emergencySetting
    if (settings == null) {
      settings = new Settings()
      settings.id = IDUtils.UUID()
      settings.key = EMERGENCY
      settings.value = JsonMapper.toJsonString(list)
      insert(settings)
    } else {
      settings.value = JsonMapper.toJsonString(list)
      update(settings)
    }
    return list
  }

}
