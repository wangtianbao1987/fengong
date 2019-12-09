package com.pachira.info.utils;

import com.pachira.info.entity.RecogniItem;
import com.pachira.info.entity.RecognizeText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: 王添宝
 * @date 2019-11-18 09:48
 */
public class MergeFile {
    public static void merge(RecognizeText rtext) {
        List<RecogniItem> item0s = rtext.getItemMap().get("R0");
        List<RecogniItem> item1s = rtext.getItemMap().get("R1");
        List<RecogniItem> items = new ArrayList<>();

        if (item0s.isEmpty()) {
            items.addAll(item1s);
            return;
        }
        if (item1s.isEmpty()) {
            items.addAll(item0s);
            return;
        }
        Iterator<RecogniItem> item0It = item0s.iterator();
        Iterator<RecogniItem> item1It = item1s.iterator();

        RecogniItem item0 = null;
        RecogniItem item1 = null;

        int indexRoleItem0 = 0;
        int indexRoleItem1 = 0;

        while (true) {
            if (item0 == null && !item0It.hasNext()) {
                addLeaveItems(items, item1It, item1, indexRoleItem1);
                break;
            }
            if (item1 == null && !item1It.hasNext()) {
                addLeaveItems(items, item0It, item0, indexRoleItem0);
                break;
            }
            item0 = (item0 == null ? item0It.next() : item0);
            item1 = (item1 == null ? item1It.next() : item1);
            if (item0.getBeginTime() < item1.getBeginTime()) {
                if (addItem2Items(items, item0, indexRoleItem0)) {
                    indexRoleItem0++;
                    item0It.remove();
                }
                item0 = null;
            } else {
                if (addItem2Items(items, item1, indexRoleItem1)) {
                    indexRoleItem1++;
                    item1It.remove();
                }
                item1 = null;
            }
        }
        rtext.setItems(items);
    }

    private static void addLeaveItems(List<RecogniItem> items, Iterator<RecogniItem> itemxIt,
            RecogniItem item, int indexRoleItemx) {
        item.setIndexRoleItem(item.getIndexRoleItem() - indexRoleItemx);
        item.setSpeakId(items.size());
        items.add(item);
        while (itemxIt.hasNext()) {
            RecogniItem itemx = itemxIt.next();
            item.setText(item.getText() + itemx.getText());
            item.setTextTrans(item.getTextTrans() + itemx.getTextTrans());
            itemxIt.remove();
        }
    }

    private static boolean addItem2Items(
            List<RecogniItem> items, RecogniItem itemx, int indexRoleItemx) {
        RecogniItem item = (items.isEmpty() ? null : items.get(items.size() - 1));
        if (item == null || !item.getRoleName().equals(itemx.getRoleName())) {
            itemx.setIndexRoleItem(itemx.getIndexRoleItem() - indexRoleItemx);
            itemx.setSpeakId(items.size());
            items.add(itemx);
            return false;
        } else {
            item.setText(item.getText() + itemx.getText());
            item.setTextTrans(item.getTextTrans() + itemx.getTextTrans());
            return true;
        }
    }
}
