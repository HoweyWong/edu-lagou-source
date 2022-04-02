package com.lagou.edu.pay.utils;

import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Piaoxu
 * @since 2019/4/22-16:37
 **/
public class XmlUtils {

    public static String mapToXml(Map<String, String> params) {
        StringBuffer buffer = new StringBuffer("<xml>");

        params.keySet().forEach(k -> {
            String value = params.get(k);
            buffer.append("<").append(k).append(">");
            if (NumberUtils.isNumber(value)) {
                buffer.append(value);
            } else {
                buffer.append("<![CDATA[").append(value).append("]]>");
            }
            buffer.append("</").append(k).append(">");
        });

        buffer.append("</xml>");
        return buffer.toString();
    }

    public static Map<String, String> xmlToMap(String xmlString) {
        Map<String, String> params = new HashMap<>();
        try {
            Document doc = DocumentHelper.parseText(xmlString);
            Element rootElement = doc.getRootElement();

            List elements = rootElement.elements();

            elements.forEach(e -> {
                Element element = (Element) e;

                params.put(element.getName(), element.getStringValue());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return params;
    }
}
