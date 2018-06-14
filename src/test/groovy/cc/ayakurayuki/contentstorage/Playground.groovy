package cc.ayakurayuki.contentstorage

import cc.ayakurayuki.contentstorage.common.util.DESUtils
import com.alibaba.fastjson.JSON
import org.junit.Test

/**
 * @author ayakurayuki
 * @date 2018/6/14-19:49
 */
class Playground {

  @Test
  void test() {
    def list = [
        new A(id: 1, context: '这是一个有中文的JSON'),
        new A(id: 2, context: 'This is a context in English.')
    ]
    def message = JSON.toJSONString(list)
    def en = DESUtils.encrypt(message)
    def de
    for (i in 1..100) {
      de = DESUtils.decrypt(en)
      if (i == 20 || i == 40 || i == 60 || i == 80) {
        list = JSON.parseArray(de, A.class)
        list.add(new A(id: i, context: "No. ${i}".toString()))
        de = JSON.toJSONString(list)
      }
      en = DESUtils.encrypt(de)
    }
    de = DESUtils.decrypt(en)
    println de
    list = JSON.parseArray(de, A.class)
    println JSON.toJSONString(list)
  }

}

class A {
  def id
  def context
}
