package cc.ayakurayuki.contentstorage.module.content.dao

import cc.ayakurayuki.contentstorage.module.content.entity.Content
import org.springframework.stereotype.Repository

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
@Repository("ContentDAO")
interface ContentDAO {

  Content get(String id)

  List<Content> list()

  List<Content> search(Content t)

  int insert(Content t)

  int update(Content t)

  int delete(Content t)

}
