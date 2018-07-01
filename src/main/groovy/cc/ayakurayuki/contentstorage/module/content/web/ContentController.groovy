package cc.ayakurayuki.contentstorage.module.content.web

import cc.ayakurayuki.contentstorage.common.base.BaseBean
import cc.ayakurayuki.contentstorage.module.content.entity.Content
import cc.ayakurayuki.contentstorage.module.content.service.ContentService
import com.alibaba.fastjson.JSON
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
@Controller
class ContentController extends BaseBean {

  @Autowired
  ContentService contentService

  @ModelAttribute
  Content get(@RequestParam(required = false) String id) {
    def content = null
    if (StringUtils.isNotBlank(id)) {
      content = contentService.get(id)
    }
    if (null == content) {
      content = new Content()
      List<Map<String, Object>> list = Lists.newArrayList()
      Map<String, Object> map = Maps.newHashMap()
      map['key'] = STRING_EMPTY
      map['value'] = STRING_EMPTY
      list.add(map)
      content.jsonData = JSON.toJSONString(list)
    }
    return content
  }

  @RequestMapping(['/', '/index'])
  def home(Model model) {
    model.addAttribute 'list', contentService.search(null)
    'index'
  }

  @RequestMapping('/search')
  def search(String item, Model model) {
    if (StringUtils.isEmpty(item)) {
      return ROOT_PATH
    }
    def list = contentService.search(item)
    model.addAttribute 'list', list
    model.addAttribute 'item', item
    return 'index'
  }

  @RequestMapping('/form')
  def form(Content content, Model model) {
    def list = JSON.parseObject(content.jsonData, List.class)
    model.addAttribute 'list', list
    model.addAttribute 'content', content
    'form'
  }

  @RequestMapping('/save')
  def save(Content content, String[] key, String[] value) {
    def list = Lists.newArrayList()
    for (def i = 0; i < key.length; i++) {
      def map = Maps.newHashMap()
      map['key'] = key[i]
      map['value'] = value[i]
      list.add(map)
    }
    content.jsonData = JSON.toJSONString(list)
    contentService.save(content.id, content.item, content.jsonData)
    ROOT_PATH
  }

  @RequestMapping('/detail')
  def detail(Content content, Model model) {
    def list = JSON.parseObject(content.jsonData, List.class)
    model.addAttribute 'list', list
    model.addAttribute 'content', content
    'detail'
  }

  @RequestMapping('/delete')
  def delete(Content content) {
    contentService.delete(content.id)
    ROOT_PATH
  }

}
