package cc.ayakurayuki.contentstorage.module.settings.service

import cc.ayakurayuki.contentstorage.common.base.BaseBean
import cc.ayakurayuki.contentstorage.common.util.GoogleAuthenticator
import cc.ayakurayuki.contentstorage.common.util.IDUtils
import cc.ayakurayuki.contentstorage.common.util.RandomUtils
import cc.ayakurayuki.contentstorage.module.settings.dao.SettingsDAO
import cc.ayakurayuki.contentstorage.module.settings.entity.EmergencyKey
import cc.ayakurayuki.contentstorage.module.settings.entity.Settings
import com.alibaba.fastjson.JSON
import com.google.common.collect.Lists
import org.apache.commons.lang3.StringUtils
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
  SettingsDAO settingsDAO

  Settings get(String id) {
    settingsDAO.get(id)
  }

  def save(Settings settings) {
    settingsDAO.save(settings)
  }

  def delete(Settings settings) {
    settingsDAO.delete(settings)
  }

  private Settings getByKey(String key) {
    settingsDAO.getByKey(key)
  }

  /**
   * 获取特征码
   * @return 特征码字符串
   */
  private String getSecretKeyFromDatabase() {
    def settings = getByKey(SECRET)
    if (null == settings) {
      settings = new Settings()
      settings.id = IDUtils.UUID()
      settings.key = SECRET
      settings.value = GoogleAuthenticator.generateSecretKey()
    } else {
      settings.value = GoogleAuthenticator.generateSecretKey()
    }
    save(settings)
    return settings.value
  }

  def validateAuthCode(String authCode) {
    def emergencyCheck = validateEmergencyCode(authCode)
    if (emergencyCheck) {
      return emergencyCheck
    }
    if (!StringUtils.isNumeric(authCode)) {
      return false
    }
    def code = Long.valueOf(authCode)
    def googleAuthenticator = new GoogleAuthenticator()
    googleAuthenticator.windowSize = 5
    def settings = getByKey(SECRET)
    return googleAuthenticator.checkCode(settings.value, code)
  }

  /**
   * 获取应急码
   * @return
   */
  List<EmergencyKey> generateEmergencyCode() {
    List<EmergencyKey> list = Lists.newArrayList()
    for (i in 1..10) {
      def key = new EmergencyKey()
      key.key = RandomUtils.emergencyKey
      key.effective = true
      list.add(key)
    }
    def settings = getByKey(EMERGENCY)
    if (null == settings) {
      settings = new Settings()
      settings.id = IDUtils.UUID()
      settings.key = EMERGENCY
      settings.value = JSON.toJSONString(list)
    } else {
      settings.value = JSON.toJSONString(list)
    }
    save(settings)
    return list
  }

  boolean validateEmergencyCode(String code) {
    def settings = getByKey(EMERGENCY)
    List<EmergencyKey> list = JSON.parseArray(settings.value, EmergencyKey.class)
    List<EmergencyKey> temp = []
    def validate = false
    for (key in list) {
      if (StringUtils.equals(code, key.key) && key.effective) {
        validate = true
        key.effective = false
      }
      temp.add(key)
    }
    settings.value = JSON.toJSONString(temp)
    save(settings)
    return validate
  }

  String getQRCode(String conditionCode) {
    GoogleAuthenticator.getQRCode(conditionCode, secretKeyFromDatabase)
  }

}
