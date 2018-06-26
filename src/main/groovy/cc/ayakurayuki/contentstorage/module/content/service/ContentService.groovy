package cc.ayakurayuki.contentstorage.module.content.service

import cc.ayakurayuki.contentstorage.common.base.BaseBean
import cc.ayakurayuki.contentstorage.common.util.IDUtils
import cc.ayakurayuki.contentstorage.module.content.dao.ContentDAO
import cc.ayakurayuki.contentstorage.module.content.entity.Content
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
@Service("ContentService")
@Transactional(readOnly = true)
class ContentService extends BaseBean {

  @Autowired
  ContentDAO dao

  /**
   * 获取全部信息
   * @return 全体信息列表
   */
  List<Content> list() {
    def list = dao.list()
    list.each {
      it.jsonData = decodeJSON(it.jsonData)
    }
    return list
  }

  /**
   * 查询信息
   * @param content 查询条件封装对象
   * @return 查询结果
   */
  List<Content> search(Content content) {
    def list = dao.search(content)
    list.each {
      it.jsonData = decodeJSON(it.jsonData)
    }
    return list
  }

  /**
   * 获取对应ID的信息
   * @param id ID
   * @return 结果对象
   */
  Content get(String id) {
    def content = dao.get(id)
    content.jsonData = decodeJSON(content.jsonData)
    return content
  }

  /**
   * 插入新信息
   * @param item
   * @param jsonData
   * @return
   */
  int insert(String item, String jsonData) {
    def content = new Content()
    content.id = IDUtils.UUID()
    content.item = item
    content.jsonData = encodeJSON(jsonData)
    dao.insert(content)
  }

  /**
   * 更新信息
   * @param id
   * @param item
   * @param jsonData
   * @return
   */
  int update(String id, String item, String jsonData) {
    def content = new Content()
    content.id = id
    content.item = item
    content.jsonData = encodeJSON(jsonData)
    dao.update(content)
  }

  /**
   * 删除信息
   * @param id
   * @return
   */
  int delete(String id) {
    dao.delete(get(id))
  }

}
