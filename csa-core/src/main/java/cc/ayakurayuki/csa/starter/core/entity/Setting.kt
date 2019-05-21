package cc.ayakurayuki.csa.starter.core.entity

import io.vertx.core.json.Json

/**
 *
 * @author ayakurayuki
 * @date 2019/05/20-17:58
 */
data class Setting(var id: String, var key: String, var value: String) {
  constructor() : this("", "", "")

  override fun toString(): String {
    return Json.encode(this)
  }
}
