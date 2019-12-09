package com.pachira.info.utils;

import com.pachira.info.entity.RecogniItem;
import com.pachira.info.entity.RecognizeText;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

/**
 * @author: 王添宝
 * @date 2019-11-13 15:15
 */
public class ReadXML {
    public static RecognizeText readXML(File xmlFile) {
        try {
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(xmlFile);
            Element root = doc.getRootElement(); // RecognizeResult
            Element speech = root.element("Speech");
            if (speech == null) {
                System.out.println("找不到Speech节点 ： " + xmlFile.getName());
                return null;
            }
            List<Element> subjects = speech.elements("Subject");
            RecognizeText rtext = new RecognizeText();
            rtext.setXmlFile(xmlFile);

            for (Element subject : subjects) {
                String subName = subject.attributeValue("Name");
                if (!"RecognizeText".equals(subName)) {
                    continue;
                }

                List<Element> roles = subject.elements("Role");
                for (int i = 0; i < roles.size(); i++) {
                    Element role = roles.get(i);
                    Element endPoint = role.element("EndPoint");
                    if (endPoint == null) {
                        continue;
                    }
                    List<Element> items = endPoint.elements("Item");
                    if (items == null || items.isEmpty()) {
                        continue;
                    }
                    String roleName = role.attributeValue("Name");
                    int indexRoleWords = 0;
                    for (int j = 0; j < items.size(); j++) {
                        Element itemEle = items.get(j);
                        String text = itemEle.elementText("Text");
                        text = text.replace(" ", "");
                        String textTrans = StringUtils.transStr(text);
                        rtext.appendSpeak(roleName, textTrans);

                        RecogniItem item = new RecogniItem();
                        item.setText(text);
                        item.setTextTrans(textTrans);
                        String begin = itemEle.attributeValue("Begin");
                        item.setBeginTime(CommonUtils.toInt(begin));
                        item.setRtext(rtext);
                        item.setIndexRoleWords(indexRoleWords);
                        item.setIndexRoleItem(j);
                        item.setRoleName(roleName);
                        rtext.addItem(roleName, item);
                        indexRoleWords += textTrans.length();
                    }
                }
            }

            MergeFile.merge(rtext);
            return rtext;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }



}
