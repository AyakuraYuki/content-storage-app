package cc.ayakurayuki.contentstorage.module.content.dao

import cc.ayakurayuki.contentstorage.module.content.entity.Content
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
@Repository('ContentDAO')
interface ContentDAO {

  Content get(String id)

  List<Content> list()

  List<Content> search(@Param('item') String item)

  int insert(Content t)

  int update(Content t)

  int delete(@Param('id') String id)

}
