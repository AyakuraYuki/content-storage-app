package cc.ayakurayuki.contentstorage.module.content.dao

import cc.ayakurayuki.contentstorage.module.content.entity.Content
import org.springframework.stereotype.Repository

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
@Repository("ContentDao")
interface ContentDao {

    Content get(String id)

    List<Content> codexList()

    List<Content> search(Content account)

    int insert(Content account)

    int update(Content account)

    int delete(Content account)

}
