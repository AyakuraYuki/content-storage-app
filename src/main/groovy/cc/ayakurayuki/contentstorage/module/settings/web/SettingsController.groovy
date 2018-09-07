package cc.ayakurayuki.contentstorage.module.settings.web

import cc.ayakurayuki.contentstorage.common.base.BaseBean
import cc.ayakurayuki.contentstorage.module.settings.service.SettingsService
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
class SettingsController extends BaseBean {

  @Autowired
  SettingsService settingsService

  @RequestMapping("/2FA")
  def twoStep() {
    "2FA"
  }

  @RequestMapping("/do2FA")
  def do2FA(String authCode, HttpServletRequest request) {
    def authentic = settingsService.validateAuthCode(authCode)
    request.session.setAttribute AUTHENTIC, authentic
    ROOT_PATH
  }

  @RequestMapping("/register2FA")
  def register2FA() {

    "register2FA"
  }

  @RequestMapping("/doRegister2FA")
  def doRegister2FA(String conditionCode, Model model) {
    model.addAttribute EMERGENCY, settingsService.generateEmergencyCode()
    model.addAttribute("QRCode", settingsService.getQRCode(conditionCode))
    "registerResult"
  }

  @RequestMapping("/doReset2FA")
  def doReset2FA(String recoveryCode, Model model) {

  }

}
