package cc.ayakurayuki.csa.starter.core.entity

import java.util.*

/**
 *
 * @author ayakurayuki
 * @date 2019/05/09-10:30
 */
data class JsonResponse(val status: Int, val message: String, val data: Any) {

  constructor() : this(JsonResponse())

  constructor(data: Any) : this(0, "", data)

  constructor(status: Int, data: Any) : this(status, "", data)

  private val result: LinkedHashMap<String, Any>
    get() {
      val map = LinkedHashMap<String, Any>()
      map["status"] = this.status
      map["message"] = this.message
      map["data"] = this.data
      return map
    }

}
