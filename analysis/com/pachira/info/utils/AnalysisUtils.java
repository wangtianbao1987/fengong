package com.pachira.info.utils;

import com.pachira.info.entity.FindObj;
import com.pachira.info.entity.RecogniItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: 王添宝
 * @date 2019-11-14 10:33
 */
public class AnalysisUtils {

    public static List<FindObj> findLocation(FindObj baseFind,
            String source, String target, List<RecogniItem> items) {
        List<Integer> indexes = StringUtils.indexOf(source, target);
        List<FindObj> findItems = findItems(baseFind, target, indexes, items);
        return findItems;
    }

    public static List<FindObj> regexLocation(FindObj baseFind,
            String source, Pattern p, List<RecogniItem> items) {
        Matcher m = p.matcher(source);
        int index = 0;
        List<FindObj> findItems = new ArrayList<>();
        while (m.find()) {
            String target = m.group();
            int startIndex = m.start();
            int endIndex = m.end();
            RecogniItem startItem = findItem(index, startIndex, items);
            RecogniItem endItem = findItem(
                    startItem.getIndexRoleItem(), endIndex - 1, items);
            FindObj findObj = baseFind.clone();
            findObj.setStartItem(startItem);
            findObj.setEndItem(endItem);
            findObj.setMatchStr(target);
            findObj.setShowStr(target);


            int startWords = StringUtils.getRealIndex(
                    startIndex - startItem.getIndexRoleWords(), startItem.getText());
            findObj.setStartWords(startWords);

            int endWords = StringUtils.getRealIndex(
                    endIndex - endItem.getIndexRoleWords() - 1,
                    endItem.getText()) + 1;
            findObj.setEndWords(endWords);

            findItems.add(findObj);
            index = endItem.getIndexRoleItem();
        }
        return findItems;
    }

    private static List<FindObj> findItems(FindObj baseFind,
            String target, List<Integer> indexes,List<RecogniItem> items) {
        List<FindObj> findItems = new ArrayList<>();
        for (Integer index : indexes) {
            RecogniItem startItem = findItem(0, index, items);
            RecogniItem endItem = findItem(
                    startItem.getIndexRoleItem(), index + target.length() - 1, items);
            FindObj findObj = baseFind.clone();
            findObj.setStartItem(startItem);
            findObj.setEndItem(endItem);
            findObj.setMatchStr(target);
            findObj.setShowStr(target);

            int startWords = StringUtils.getRealIndex(
                    index - startItem.getIndexRoleWords(), startItem.getText());
            findObj.setStartWords(startWords);

            int endWords = StringUtils.getRealIndex(
                    index + target.length() - endItem.getIndexRoleWords() - 1,
                    endItem.getText()) + 1;
            findObj.setEndWords(endWords);

            findItems.add(findObj);
        }
        return findItems;
    }

    /**
     * @param left      开始查找的会话
     * @param index     要查找的字符索引
     * @param items     会话列表
     * @return          要查找的字符索引所在的会话项
     */
    public static RecogniItem findItem(int left, Integer index, List<RecogniItem> items) {
        int right = items.size() - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            RecogniItem midItem = items.get(mid);
            int midIndexRoleWords = midItem.getIndexRoleWords();
            if (midIndexRoleWords > index) {
                right = mid - 1;
            } else {
                int itemWordsLen = midItem.getTextTrans().length();
                if (midIndexRoleWords + itemWordsLen <= index) {
                    left = mid + 1;
                } else {
                    return midItem;
                }
            }
        }
        return items.get((left + right) / 2);

    }
}
