package com.pachira.info.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pachira.info.entity.FindObj;
import com.pachira.info.entity.RecogniItem;

/**
 * @author: 王添宝
 * @date 2019-11-15 10:03
 */
public class AddressUtils {

    public static List<FindObj> findAddr(String speak, List<RecogniItem> items, FindObj baseFind) {
        Map<String, List<Integer>> indexMap = new HashMap<String, List<Integer>>();
        for (int i = 0; i < Sheng.names.size(); i++) {
            List<Integer> indexList = StringUtils.indexOf(speak, Sheng.names.get(i));
            if (!indexList.isEmpty()) {
                indexMap.put(Sheng.names.get(i), indexList);
            }
        }
        List<FindObj> res = new ArrayList<>();
        if (indexMap.isEmpty()) {
            return res;
        }
        Iterator<String> indexIt = indexMap.keySet().iterator();

        while (indexIt.hasNext()) {
            String shengName = indexIt.next();
            List<Integer> indexes = indexMap.get(shengName);
            for (Integer index : indexes) {
                String addrAround = null;
                if (speak.length() > index + 40) {
                    // 取40个字，从这40个字中找地址
                    addrAround = speak.substring(index, index + 41);
                } else {
                    addrAround = speak.substring(index);
                }
                int endIndex = -1;
                for (int i = addrAround.length() - 1; i > shengName.length(); i--) {
                    char ch = addrAround.charAt(i);
                    if (ch == '省' || ch == '市' || ch == '区' || ch == '县' || ch == '镇' || ch == '乡'
                            || ch == '村' || ch == '庄' || ch == '道' || ch == '路' || ch == '厦'
                            || ch == '号') {
                        if ((ch == '道' || ch == '厦') && addrAround.charAt(i-1) != '大') {
                            continue;
                        }
                        endIndex = i + 1;
                        break;
                    }
                }
                String matchStr = shengName;
                if (endIndex != -1) {
                    matchStr = addrAround.substring(0, endIndex);
                }

                RecogniItem startItem = AnalysisUtils.findItem(0, index, items);
                RecogniItem endItem = AnalysisUtils.findItem(
                        startItem.getIndexRoleItem(), index + matchStr.length() - 1, items);


                FindObj findObj = baseFind.clone();
                findObj.setStartItem(startItem);
                findObj.setEndItem(endItem);
                findObj.setMatchStr(matchStr);
                findObj.setShowStr(matchStr);

                int startWords = StringUtils.getRealIndex(
                        index - startItem.getIndexRoleWords(), startItem.getText());
                findObj.setStartWords(startWords);

                int endWords = StringUtils.getRealIndex(
                        index + matchStr.length() - endItem.getIndexRoleWords() - 1,
                        endItem.getText()) + 1;
                findObj.setEndWords(endWords);

                res.add(findObj);
            }
        }
        return res;
    }
}
