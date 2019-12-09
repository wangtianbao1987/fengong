package com.pachira.info.entity;

import com.pachira.info.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: 王添宝
 * @date 2019-11-13 15:04
 */
@Getter
@Setter
public class FindObj implements Cloneable {
    // 查找类型
    private String type;
    // 查找的类型名称
    private String typeName;
    // 匹配的字符串（不包含标点）
    private String matchStr;
    // 展示的字符串
    private String showStr;
    // 索引开始的会话项
    private RecogniItem startItem;
    // 索引结束的会话项
    private RecogniItem endItem;
    // 索引开始的会话项中的索引位置（包含标点）
    private int startWords;
    // 索引结束的会话项中的索引位置（包含标点）
    private int endWords;

    private static final Pattern pattern_num = Pattern.compile("([0-9]*[十百千万][0-9]*)+");

    public void setShowStr(String showStr) {
        Matcher m = pattern_num.matcher(showStr);
        StringBuffer res = new StringBuffer();
        int preEnd = 0;
        while (m.find()) {
            String matchStr = m.group();
            if (!CommonUtils.containsNum(matchStr)) {
                continue;
            }
            int startIndex = m.start();
            String s1 = CommonUtils.transNum(matchStr);
            res.append(showStr.substring(preEnd, startIndex)).append(s1);
            preEnd = m.end();
        }
        if (preEnd != showStr.length()) {
            res.append(showStr.substring(preEnd));
        }
        this.showStr = res.toString();
    }

    @Override
    public FindObj clone() {
        try {
            return (FindObj)super.clone();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
