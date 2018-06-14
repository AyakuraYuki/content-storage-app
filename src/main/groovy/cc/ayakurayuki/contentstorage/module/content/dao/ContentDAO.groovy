package cc.ayakurayuki.contentstorage.module.content.dao

import cc.ayakurayuki.contentstorage.common.base.BaseDAO
import cc.ayakurayuki.contentstorage.module.content.entity.Content
import org.springframework.stereotype.Repository

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
@Repository("ContentDAO")
interface ContentDAO extends BaseDAO<Content> {
}
