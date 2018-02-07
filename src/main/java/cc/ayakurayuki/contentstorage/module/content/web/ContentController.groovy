package cc.ayakurayuki.contentstorage.module.content.web

import cc.ayakurayuki.contentstorage.common.base.BaseBean
import cc.ayakurayuki.contentstorage.common.util.JsonMapper
import cc.ayakurayuki.contentstorage.module.content.entity.Content
import cc.ayakurayuki.contentstorage.module.content.service.ContentService
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
        if (content == null) {
            content = new Content()
            List<HashMap<String, String>> list = Lists.newArrayList()
            HashMap<String, String> map = Maps.newHashMap()
            map['key'] = ''
            map['value'] = ''
            list.add(map)
            content.json_data = JsonMapper.toJsonString(list)
        }
        return content
    }

    @RequestMapping("/")
    def home(Model model) {
        model.addAttribute "list", contentService.list()
        "index"
    }

    @RequestMapping("/search")
    def search(String item, Model model) {
        def content = new Content()
        content.item = item
        def list = contentService.search(content)
        model.addAttribute "list", list
        "index"
    }

    @RequestMapping("/form")
    def form(Content content, Model model) {
        def list = JsonMapper.fromJsonString(content.json_data, List.class)
        model.addAttribute "list", list
        model.addAttribute "content", content
        "form"
    }

    @RequestMapping("/save")
    def save(Content content, String[] key, String[] value) {
        def list = Lists.newArrayList()
        for (def i = 0; i < key.length; i++) {
            def map = Maps.newHashMap()
            map['key'] = key[i]
            map['value'] = value[i]
            list.add(map)
        }
        content.json_data = JsonMapper.toJsonString(list)
        if (StringUtils.isBlank(content.id)) {
            contentService.insert content.item, content.json_data
        } else if (StringUtils.isNotBlank(content.id) && contentService.get(content.id) == null) {
            contentService.insert content.item, content.json_data
        } else {
            contentService.update content.id, content.item, content.json_data
        }
        "redirect:/"
    }

    @RequestMapping("/detail")
    def detail(Content content, Model model) {
        def list = JsonMapper.fromJsonString(content.json_data, List.class)
        model.addAttribute "list", list
        model.addAttribute "content", content
        "detail"
    }

    @RequestMapping("/delete")
    def delete(Content content) {
        contentService.delete(content.id)
        "redirect:/"
    }

}
