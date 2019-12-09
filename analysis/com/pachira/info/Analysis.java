package com.pachira.info;

import com.pachira.info.entity.FindObj;
import com.pachira.info.entity.RecogniItem;
import com.pachira.info.entity.RecognizeText;
import com.pachira.info.utils.AddressUtils;
import com.pachira.info.utils.AnalysisUtils;
import com.pachira.info.utils.BankUtils;
import com.pachira.info.utils.CommonUtils;
import com.pachira.info.utils.IdCardUtil;
import com.pachira.info.utils.ReadXML;
import com.pachira.info.utils.TxtConf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author: 王添宝
 * @date 2019-11-13 18:08
 */
public class Analysis {
    private static TxtConf conf = TxtConf.getInstance("conf.txt");
    public static RecognizeText analysis(File xmlFile) {
        RecognizeText rtext = ReadXML.readXML(xmlFile);
        if (rtext == null) {
            return null;
        }
        String[] types = conf.get("search.types").split(",");
        for (String type : types) {
            String typeName = conf.get("search." + type + ".typeName");
            FindObj baseFind = new FindObj();
            baseFind.setType(type);
            baseFind.setTypeName(typeName);

            List<FindObj> findList = targetStr(rtext, baseFind);
            manageFinds(rtext.getFindObjs(), findList);
            findList = targetRegex(rtext, baseFind);
            manageFinds(rtext.getFindObjs(), findList);
            findList = targetType(rtext, baseFind);
            manageFinds(rtext.getFindObjs(), findList);
        }
        return rtext;
    }

    public static void manageFinds(List<FindObj> contains, List<FindObj> findList) {
        if (!CommonUtils.isEmptyCollection(findList)) {
            checkType(findList);
            if (findList.isEmpty()) {
                return;
            }
            val2val(findList);
            transVal(findList);
            contains.addAll(findList);
        }
    }

    public static List<FindObj> targetStr(RecognizeText rtext, FindObj baseFind){
        String targetStrs = conf.get("search." + baseFind.getType() + ".target.str");
        if (CommonUtils.isEmpty(targetStrs)) {
            return null;
        }
        String[] targets = targetStrs.split("\\|");
        List<FindObj> findObjs = new ArrayList<>();
        String r0 = rtext.getSpeaks().get("R0").toString();
        String r1 = rtext.getSpeaks().get("R1").toString();
        List<RecogniItem> r0Item = rtext.getItemMap().get("R0");
        List<RecogniItem> r1Item = rtext.getItemMap().get("R1");
        for (String target : targets) {
            findObjs.addAll(
                    AnalysisUtils.findLocation(baseFind, r0, target, r0Item)
            );
            findObjs.addAll(
                    AnalysisUtils.findLocation(baseFind, r1, target, r1Item)
            );
        }
        return findObjs;
    }

    public static List<FindObj> targetRegex(RecognizeText rtext, FindObj baseFind) {
        String regex = conf.get("search." + baseFind.getType() + ".target.regex");
        if (CommonUtils.isEmpty(regex)) {
            return null;
        }
        List<FindObj> findObjs = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        String r0 = rtext.getSpeaks().get("R0").toString();
        String r1 = rtext.getSpeaks().get("R1").toString();
        List<RecogniItem> r0Item = rtext.getItemMap().get("R0");
        List<RecogniItem> r1Item = rtext.getItemMap().get("R1");
        findObjs.addAll(
                AnalysisUtils.regexLocation(baseFind, r0, p, r0Item)
        );
        findObjs.addAll(
                AnalysisUtils.regexLocation(baseFind, r1, p, r1Item)
        );
        return findObjs;
    }

    public static List<FindObj> targetType(RecognizeText rtext, FindObj baseFind) {
        String targetType = conf.get("search." + baseFind.getType() + ".target.type");
        if (CommonUtils.isEmpty(targetType)) {
            return null;
        }
        List<FindObj> findObjs = new ArrayList<>();
        String r0 = rtext.getSpeaks().get("R0").toString();
        String r1 = rtext.getSpeaks().get("R1").toString();
        List<RecogniItem> r0Item = rtext.getItemMap().get("R0");
        List<RecogniItem> r1Item = rtext.getItemMap().get("R1");
        if ("address".equals(targetType)) {
            findObjs.addAll(
                    AddressUtils.findAddr(r0, r0Item, baseFind)
            );
            findObjs.addAll(
                    AddressUtils.findAddr(r1, r1Item, baseFind)
            );
        }
        return findObjs;
    }


    public static void checkType(List<FindObj> findList) {
        String type = findList.get(0).getType();
        String checkType = conf.get("search." + type + ".checkType");
        if (checkType == null) {
            return;
        }
        if ("idCard".equals(checkType)) {
            // 身份证校验
            for (int i = findList.size() - 1; i >= 0; i--) {
                FindObj findObj = findList.get(i);
                if (!IdCardUtil.isValidatedAllIdcard(findObj.getMatchStr())) {
                    findList.remove(i);
                }
            }
            return;
        }
        if ("yinlian".equals(checkType)) {
            // 银联卡校验
            for (int i = findList.size() - 1; i >= 0; i--) {
                FindObj findObj = findList.get(i);
                if (!BankUtils.isYinlian(findObj.getMatchStr())) {
                    findList.remove(i);
                }
            }
        }
    }

    public static void val2val(List<FindObj> findList) {
        String type = findList.get(0).getType();
        String val2val = conf.get("search." + type + ".val2val");
        if (val2val == null) {
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        String[] findVals = val2val.split("\\|");
        for (String vals : findVals) {
            String[] valArr = vals.split("=");
            if (valArr.length == 2) {
                map.put(valArr[0], valArr[1]);
            }
        }
        for (FindObj findObj : findList) {
            String showStr = map.get(findObj.getMatchStr());
            if (showStr != null) {
                findObj.setShowStr(showStr);
            }
        }
    }

    public static void transVal(List<FindObj> findList) {
        String type = findList.get(0).getType();
        String transVal = conf.get("search." + type + ".transVal");
        if (transVal == null) {
            return;
        }
        if ("number".equals(transVal)) {
            for (FindObj findObj : findList) {
                String showStr = CommonUtils.transNum(findObj.getMatchStr());
                if (showStr != null) {
                    findObj.setShowStr(showStr);
                }
            }
        }
    }





}
