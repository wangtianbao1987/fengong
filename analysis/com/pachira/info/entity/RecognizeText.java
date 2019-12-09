package com.pachira.info.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 王添宝
 * @date 2019-11-13 15:03
 */
@Getter
@Setter
public class RecognizeText {
    // 解析的xml文件
    private File xmlFile;
    // <角色名, 会话列表>
    private Map<String, List<RecogniItem>> itemMap;
    // <角色名, 全部会话内容>
    private Map<String, StringBuffer> speaks;
    // 查找命中目标列表
    private List<FindObj> findObjs;
    // 经过合并并排序的会话列表（包含角色R0与角色R1，只有调用了Merge操作，此属性才会有数据，并且itemMap中的数据也会经过合并）
    private List<RecogniItem> items;

    public RecognizeText() {
        this.itemMap = new HashMap<String, List<RecogniItem>>();
        this.itemMap.put("R0", new ArrayList<RecogniItem>());
        this.itemMap.put("R1", new ArrayList<RecogniItem>());
        this.speaks = new HashMap<String, StringBuffer>();
        this.speaks.put("R0", new StringBuffer());
        this.speaks.put("R1", new StringBuffer());
        this.findObjs = new ArrayList<FindObj>();
    }

    public RecognizeText appendSpeak(String roleName, String speak) {
        this.speaks.get(roleName).append(speak);
        return this;
    }

    public RecognizeText addItem(String roleName, RecogniItem item) {
        this.itemMap.get(roleName).add(item);
        return this;
    }

}
