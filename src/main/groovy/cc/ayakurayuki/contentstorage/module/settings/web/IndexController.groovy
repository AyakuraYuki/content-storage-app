package cc.ayakurayuki.contentstorage.module.settings.web

import cc.ayakurayuki.contentstorage.common.base.BaseBean
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 *
 * @author ayakurayuki
 * @date 2018/11/10-16:39
 */
@Controller
class IndexController extends BaseBean {

  @RequestMapping('/')
  def root() {
    ROOT_PATH
  }

}
