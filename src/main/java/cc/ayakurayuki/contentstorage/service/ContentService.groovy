package cc.ayakurayuki.contentstorage.service

import cc.ayakurayuki.contentstorage.dao.ContentDao
import cc.ayakurayuki.contentstorage.entity.Content
import cc.ayakurayuki.contentstorage.utils.DESUtils
import cc.ayakurayuki.contentstorage.utils.IDUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
@Service("contentService")
class ContentService {

    @Autowired
    ContentDao contentDao

    /**
     * 获取全部信息
     * @return 全体信息列表
     */
    List<Content> codexList() {
        def list = contentDao.codexList()
        for (def item : list) {
            item.json_data = DESUtils.decrypt(item.json_data)
        }
        return list
    }

    /**
     * 查询信息
     * @param content 查询条件封装对象
     * @return 查询结果
     */
    List<Content> search(Content content) {
        def list = contentDao.search(content)
        for (def item : list) {
            item.json_data = DESUtils.decrypt(item.json_data)
        }
        return list
    }

    /**
     * 获取对应ID的信息
     * @param id ID
     * @return 结果对象
     */
    Content get(String id) {
        def content = contentDao.get(id)
        content.json_data = DESUtils.decrypt(content.json_data)
        return content
    }

    /**
     * 插入新信息
     * @param item
     * @param json_data
     * @return
     */
    int insert(String item, String json_data) {
        def content = new Content()
        content.id = IDUtils.UUID()
        content.item = item
        content.json_data = DESUtils.encrypt(json_data)
        contentDao.insert(content)
    }

    /**
     * 更新信息
     * @param id
     * @param item
     * @param json_data
     * @return
     */
    int update(String id, String item, String json_data) {
        def content = new Content()
        content.id = id
        content.item = item
        content.json_data = DESUtils.encrypt(json_data)
        contentDao.update(content)
    }

    /**
     * 删除信息
     * @param id
     * @return
     */
    int delete(String id) {
        contentDao.delete(get(id))
    }

}
