package cc.ayakurayuki.contentstorage.module.settings.web

import cc.ayakurayuki.contentstorage.common.base.Base
import cc.ayakurayuki.contentstorage.common.exception.AuthException
import cc.ayakurayuki.contentstorage.common.exception.StatusCodeException
import cc.ayakurayuki.contentstorage.module.settings.service.SettingsService
import com.arronlong.httpclientutil.HttpClientUtil
import com.arronlong.httpclientutil.common.HttpConfig
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpServletRequest

/**
 * Created by ayakurayuki on 2018/2/7-11:14. <br/>
 * Package: cc.ayakurayuki.contentstorage.module.settings.web <br/>
 */
@Controller
@RequestMapping(value = '/system')
class SettingsController extends Base {

  final static def LOGGER = LogManager.getLogger SettingsService.class

  @Autowired
  SettingsService settingsService

  @RequestMapping('2FA')
  def twoStep() { '2FA' }

  @RequestMapping('access')
  def access(String authCode, HttpServletRequest request) {
    if (settingsService.validateAuthCode(authCode)) {
      request.session.setAttribute AUTHENTIC, Boolean.TRUE
      return ROOT_PATH
    }
    throw new AuthException('Verify code error!')
  }

  @RequestMapping('register')
  def register2FA() {
    if (settingsService.getByKey(SECRET) != null) {
      return 'reset2FA'
    }
    return 'register2FA'
  }

  @RequestMapping('doRegister2FA')
  def doRegister2FA(String conditionCode, Model model) {
    model.addAttribute EMERGENCY, settingsService.generateEmergencyCode()
    model.addAttribute("QRCode", settingsService.getQRCode(conditionCode))
    'registerResult'
  }

  @RequestMapping('doReset2FA')
  def doReset2FA(String recoveryCode, Model model) {
    if (settingsService.isAllEmergencyCodeUsed()) {
      throw new StatusCodeException(ErrorCode.ALL_EMERGENCY_CODE_USED.code, 'All emergency code has been used!')
    }
    if (settingsService.validateEmergencyCode(recoveryCode)) {
      return 'register2FA'
    }
    throw new AuthException('Recovery code error!')
  }

  @RequestMapping('exit')
  def exit(HttpServletRequest request) {
    request.session.setAttribute AUTHENTIC, Boolean.FALSE
    LOGGER.info 'See you next time~'
    ROOT_PATH
  }

  @RequestMapping('shutdown')
  def shutdown(HttpServletRequest request) {
    request.session.setAttribute AUTHENTIC, Boolean.FALSE
    LOGGER.info 'See you next time~'
    HttpClientUtil.post(HttpConfig.custom().url('http://localhost:8889/actuator/shutdown'))
    'redirect:about:blank'
  }

}
