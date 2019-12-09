package com.pachira.info.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: 王添宝
 * @date 2019-11-13 15:04
 */
@Getter
@Setter
public class RecogniItem {
    // 包含两个角色的顺序ID,(只有在合并的时候才会有该数值)
    private int speakId;
    private int beginTime;
    // 在本角色列表中的索引位置
    private int indexRoleItem;
    // 在本角色列表中开始的字符索引位置（不包含标点与空格）
    private int indexRoleWords;
    // 原始文本（去空格）
    private String text;
    // 去空格，转数字，去标点
    private String textTrans;
    private String roleName;
    private RecognizeText rtext;
}
