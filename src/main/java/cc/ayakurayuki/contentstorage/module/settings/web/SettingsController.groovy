package cc.ayakurayuki.contentstorage.module.settings.web

import cc.ayakurayuki.contentstorage.common.base.BaseBean
import cc.ayakurayuki.contentstorage.common.util.GoogleAuthenticator
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

    @RequestMapping("/two-step")
    def twoStep() {
        "twoFA"
    }

    @RequestMapping("/two-step-auth")
    def twoStepAuth(String authCode, HttpServletRequest request) {
        def code = Long.parseLong(authCode)
        def time = System.currentTimeMillis()
        def googleAuthenticator = new GoogleAuthenticator()
        googleAuthenticator.windowSize = 5
        def settings = settingsService.secretSetting
        def authentic = googleAuthenticator.check_code(settings.value, code, time)
        request.session.setAttribute "authentic", authentic
        "redirect:/"
    }

    @RequestMapping("/register-two-step")
    def registerTwoStep() {
        "register-two-step"
    }

    @RequestMapping("/do-register-two-step")
    def doRegisterTwoStep(String conditionCode, Model model) {
        model.addAttribute(EMERGENCY, settingsService.generateEmergencyCode())
        model.addAttribute(
                "QRCode",
                GoogleAuthenticator.getQRBarcode(
                        conditionCode,
                        settingsService.secretKeyFromDatabase
                )
        )
        "register-result"
    }

}
