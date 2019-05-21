package cc.ayakurayuki.csa.starter.core.entity

import io.vertx.core.json.Json

/**
 *
 * @author ayakurayuki
 * @date 2019/05/20-18:00
 */
data class EmergencyKey(val key: String, var effective: Boolean) {
  override fun toString(): String {
    return Json.encode(this)
  }
}
