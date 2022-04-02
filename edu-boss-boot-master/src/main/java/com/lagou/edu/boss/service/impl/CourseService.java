package com.lagou.edu.boss.service.impl;

import com.lagou.edu.boss.service.ICourseService;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * Author:   mkp
 * Date:     2020/6/27 15:06
 * Description: 课程
 */
@Service
public class CourseService implements ICourseService {

    public final static String host_prefix = "http://www.lgstatic.com/";
    public final static String https_host_prefix = "https://www.lgstatic.com/";
    @Override
    public void removeImgWidthAttribute(Element element) {
        if (element.tagName().equals("img")) {

            String attr = element.attr("src");
            if (StringUtils.isNotEmpty(attr) && !attr.startsWith(host_prefix) && !attr.startsWith(https_host_prefix)) {
                throw new RuntimeException("图片未完全保存成功！");
            }

            element.attr("width", "100%");
            element.attr("height", "100%");
        }
        Elements childs = element.select("> *");
        if (childs != null && !childs.isEmpty()) {
            for (Element child : childs) {
                removeImgWidthAttribute(child);
            }
        }
    }
}
