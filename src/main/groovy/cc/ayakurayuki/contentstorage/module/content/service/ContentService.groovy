package cc.ayakurayuki.contentstorage.module.content.service

import cc.ayakurayuki.contentstorage.common.base.Base
import cc.ayakurayuki.contentstorage.common.util.IDUtils
import cc.ayakurayuki.contentstorage.module.content.dao.ContentDAO
import cc.ayakurayuki.contentstorage.module.content.entity.Content
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Ayakura Yuki
 * @date 2017/9/30
 */
@Service("ContentService")
@Transactional(readOnly = true)
class ContentService extends Base {

  final static def LOGGER = LogManager.getLogger ContentService.class

  @Autowired
  ContentDAO contentDAO

  /**
   * Search content by item (also called "title")
   * @param item Key-Word. It will list all contents if this attribute is null.
   */
  List<Content> search(String item) {
    def list = contentDAO.search(item)
    list.each {
      it.jsonData = decodeJSON(it.jsonData)
    }
    return list
  }

  /**
   * Get model (also called "detail") by ID.
   */
  Content get(String id) {
    def content = contentDAO.get(id)
    if (null == content) {
      return null
    }
    content.jsonData = decodeJSON(content.jsonData)
    return content
  }

  /**
   * Save content, including insert and update.
   * @param id
   * @param item item name (also called "title")
   * @param jsonData json string built before transfer in here.
   */
  int save(String id, String item, String jsonData) {
    def content = get(id)
    if (null == content) {
      content = new Content()
      content.id = IDUtils.UUID()
    }
    if (StringUtils.isNotEmpty(item)) {
      content.item = item
    }
    if (StringUtils.isNotEmpty(jsonData)) {
      content.jsonData = encodeJSON(jsonData)
    }
    def result = contentDAO.insert(content)
    LOGGER.info "Insert a new content [id: ${content.id}] successful. Result: ${result}".toString()
    return ResponseCode.OK.code
  }

  /**
   * Delete content.
   * @param id
   */
  int delete(String id) {
    if (null == get(id)) {
      return ResponseCode.NULL.code
    }
    def result = contentDAO.delete(id)
    LOGGER.info "Delete content [id: $id] successful. Result: $result".toString()
    return ResponseCode.OK.code
  }

}
