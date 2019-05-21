package cc.ayakurayuki.csa.starter.core.entity

import io.vertx.core.json.Json

/**
 *
 * @author ayakurayuki
 * @date 2019/05/20-17:55
 */
data class Content(var id: String, var item: String, var jsonData: String) {
  constructor() : this("", "", "")

  override fun toString(): String {
    return Json.encode(this)
  }
}
