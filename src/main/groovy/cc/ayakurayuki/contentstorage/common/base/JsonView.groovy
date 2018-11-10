package cc.ayakurayuki.contentstorage.common.base

import com.alibaba.fastjson.JSONObject

/**
 *
 * @author ayakurayuki
 * @date 2018/11/10-16:21
 */
class JsonView {

  int status
  String message
  Object data

  JsonView() {
    this(new JSONObject())
  }

  JsonView(Object data) {
    this(0, data)
  }

  JsonView(int status, Object data) {
    this.status = status
    this.data = data
    this.message = ''
  }

  JsonView(int status, Object data, String message) {
    this.status = status
    this.data = data
    this.message = message
  }

  protected Map<String, Object> getResult() {
    Map<String, Object> map = new LinkedHashMap<>()
    map.put("status", this.getStatus())
    map.put("message", this.message)
    map.put("data", this.getData())
    return map
  }

}
