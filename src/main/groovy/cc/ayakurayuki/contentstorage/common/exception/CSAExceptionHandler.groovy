package cc.ayakurayuki.contentstorage.common.exception

import cc.ayakurayuki.contentstorage.common.base.BaseBean
import cc.ayakurayuki.contentstorage.common.base.JsonView
import org.apache.commons.lang3.StringUtils
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 *
 * @author ayakurayuki
 * @date 2018/11/10-16:10
 */
@ControllerAdvice
class CSAExceptionHandler extends BaseBean {

  @ExceptionHandler
  def handler(Exception e, Model model) {
    def view = [
        status : -1,
        message: StringUtils.EMPTY,
        data   : null
    ] as JsonView
    if (e instanceof CSAException) {
      view.status = e.status
      view.message = e.localizedMessage
    } else {
      view.status = ErrorCode.OTHERS.code
      view.message = e.localizedMessage
    }
    model.addAttribute('view', view)
    return 'error'
  }

}
