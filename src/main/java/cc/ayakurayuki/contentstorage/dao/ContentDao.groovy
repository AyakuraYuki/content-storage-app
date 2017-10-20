package cc.ayakurayuki.contentstorage.dao

import cc.ayakurayuki.contentstorage.entity.Content
import org.springframework.stereotype.Repository

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
@Repository("AccountDao")
interface ContentDao {

    Content get(String id)

    List<Content> codexList()

    List<Content> search(Content account)

    int insert(Content account)

    int update(Content account)

    int delete(Content account)

}
