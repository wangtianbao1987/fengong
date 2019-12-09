package com.pachira.info.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 王添宝
 * @date 2019-11-13 18:09
 */
public class TxtConf {
    private static Map<String, TxtConf> map = new HashMap<String,TxtConf>();
    private String classpathFile;
    private Map<String, String> dataMap = new HashMap<String, String>();
    private TxtConf(String classpathFile){
        this.classpathFile = classpathFile;
    }

    public static TxtConf getInstance(String classpathFile) {
        TxtConf conf = map.get(classpathFile);
        if (conf == null) {
            System.out.println("加载配置文件 ： " + classpathFile);
            conf = new TxtConf(classpathFile);
            map.put(classpathFile, conf);
            conf.loadData();
        }
        return conf;
    }

    public void changeVal(String key, String val) {
        dataMap.put(key, val);
    }

    public String get(String key) {
        return dataMap.get(key);
    }

    private void loadData() {
        BufferedReader br = null;
        InputStreamReader isr = null;
        InputStream in = null;
        try {
            in = TxtConf.class.getResourceAsStream("/" + classpathFile);
            isr = new InputStreamReader(in, "UTF-8");
            br = new BufferedReader(isr);
            String lineStr = null;
            while((lineStr = br.readLine()) != null) {
                lineStr = lineStr.trim();
                if (lineStr.startsWith("#")) {
                    continue;
                }
                int index = lineStr.indexOf("=");
                if (index != -1) {
                    dataMap.put(
                            lineStr.substring(0, index).trim(),
                            lineStr.substring(index + 1).trim()
                    );
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            map.remove(classpathFile);
            dataMap.clear();
        } finally {
            CommonUtils.close(br, isr, in);
        }
    }

    



}
