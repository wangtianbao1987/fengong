package com.pachira.info;

import com.pachira.info.entity.FindObj;
import com.pachira.info.entity.RecogniItem;
import com.pachira.info.entity.RecognizeText;
import com.pachira.info.utils.CommonUtils;
import com.pachira.info.utils.FileUtils;
import com.pachira.info.utils.TxtConf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author: 王添宝
 * @date 2019-11-15 17:52
 */
public class Main {
    private static String resTarget = "res12";
    private static int targetCount = 200/7 + 1;
    private static int tryCount = 50000;
    private static int preCha = 0;

    public static final int TARGETCOUNT = 200/7 + 1;
    public static final int TRYCOUNT = 5000;

    public static OutputStream out = null;

    public static Set<String> analyFiles = new HashSet<>();

    public static Map<String, List<File>> RES = new HashMap<String, List<File>>();
    public static TxtConf conf = TxtConf.getInstance("conf.txt");

    static {
        try {
            File logFile = new File("E:\\测试数据\\" + resTarget, "result.log");
            File logDir = logFile.getParentFile();
            if (!logDir.isDirectory()) {
                logDir.mkdirs();
            }
            out = new FileOutputStream(logFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        test02("E:\\测试数据\\200files");
    }


    public static void test02(String xmlDir) {
        File dir = new File(xmlDir);
        File[] xmlFiles = dir.listFiles();
        for (File xmlFile : xmlFiles) {
            System.out.println("解析文件：" + xmlFile.getAbsolutePath());
            printRes(xmlFile,out);
        }
    }

    public static void test00() throws Throwable {

        targetCount = TARGETCOUNT + preCha;
        preCha = 0;
        conf.changeVal("search.types", "yinlian");
        randomResult();

        targetCount = TARGETCOUNT + preCha;
        preCha = 0;
        conf.changeVal("search.types", "idCard");
        randomResult();

        targetCount = TARGETCOUNT + preCha;
        preCha = 0;
        conf.changeVal("search.types", "jobNum");
        randomResult();

        targetCount = TARGETCOUNT + preCha;
        preCha = 0;
        conf.changeVal("search.types", "age");
        randomResult();

        targetCount = TARGETCOUNT + preCha;
        preCha = 0;
        conf.changeVal("search.types", "address");
        randomResult();

        targetCount = TARGETCOUNT + preCha;
        preCha = 0;
        conf.changeVal("search.types", "tel");
        randomResult();

        targetCount = TARGETCOUNT + preCha;
        preCha = 0;
        conf.changeVal("search.types", "sex");
        randomResult();

        CommonUtils.close(out);

        Iterator<String> RESIT = RES.keySet().iterator();
        FileOutputStream fos = new FileOutputStream(
                new File("E:\\测试数据\\" + resTarget, "fileList.txt"));
        while (RESIT.hasNext()) {
            String type = RESIT.next();
            CommonUtils.println(fos,"====================" + type + "====================");
            List<File> fileList = RES.get(type);
            for (File file : fileList) {
                CommonUtils.println(fos,file.getAbsolutePath());
            }
        }
        CommonUtils.close(fos);
    }

    public static void test01() throws Throwable {
        FileReader fr = new FileReader(
                new File("E:\\测试数据\\res11", "fileList.txt"));
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        Set<String> files = new HashSet<>();
        while ((line=br.readLine()) != null && files.size() < 200) {
            line = line.trim();
            if ("".equals(line) || line.startsWith("======")) {
                continue;
            }
            files.add(line);
            File sourceFile = new File(line);
            FileUtils.copyFile(sourceFile,
                    new File("E:\\测试数据\\res11\\resfiles\\" + sourceFile.getName()));
        }
        System.out.println(line);
        CommonUtils.close(br, fr);
        CommonUtils.close(out);
        System.out.println("end....");
    }

    public static void showAll() {
        try {
            File[] xmlDirs = new File("E:\\测试数据\\xml").listFiles();
            for (File xmlDir : xmlDirs) {
                System.out.println("解析目录：" + xmlDir.getName());
                File[] xmlFiles = xmlDir.listFiles();
                for (File xmlFile : xmlFiles) {
                    printRes(xmlFile,out);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CommonUtils.close(out);
        }

    }

    public static void randomResult() {
        try {
            while(targetCount > 0) {
                File[] dirs = new File("E:\\测试数据\\xml").listFiles();
                File dir = dirs[new Random().nextInt(dirs.length)];

                File[] xmlFiles = dir.listFiles();
                File xmlFile = xmlFiles[new Random().nextInt(xmlFiles.length)];

                if (analyFiles.contains(xmlFile.getAbsolutePath())) {
                    continue;
                }
                System.out.println("解析文件：" + xmlFile.getAbsolutePath());
                printRes(xmlFile,out);
            }
            System.out.println("..........All End ........");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static void testXmlFile(File dir, String xmlName) {
        try {
            File xmlFile = new File(dir, xmlName);
            printRes(xmlFile,out);
            System.out.println("解析完毕：" + xmlFile.getName());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static void printRes(File xmlFile, OutputStream out) {
        RecognizeText rtext = Analysis.analysis(xmlFile);
        if (rtext == null) {
            return;
        }

        tryCount--;
        if (tryCount < 0) {
            preCha = targetCount;
            targetCount = 0;
            tryCount = TRYCOUNT;
        }


        List<FindObj> findObjs = rtext.getFindObjs();
        if (findObjs.isEmpty()) {
            return;
        }

        targetCount--;
        analyFiles.add(xmlFile.getAbsolutePath());

        FileUtils.copyFile(xmlFile,
                new File("E:\\测试数据\\" + resTarget + "\\xmlfiles", xmlFile.getName()));
        FileUtils.wirteSessionsToFile(rtext,
                new File("E:\\测试数据\\" + resTarget + "\\sessfiles", xmlFile.getName() + ".txt"));

        List<File> listFile = RES.get(conf.get("search.types"));
        if (listFile == null) {
            listFile = new ArrayList<>();
            RES.put(conf.get("search.types"), listFile);
        }
        listFile.add(xmlFile);

        CommonUtils.println(out,"============" + rtext.getXmlFile().getName() + "==================");
        for (FindObj findObj : findObjs) {
            StringBuffer sb = new StringBuffer();
            RecogniItem startItem = findObj.getStartItem();
            RecogniItem endItem = findObj.getEndItem();

            for (int i = startItem.getIndexRoleItem(); i <= endItem.getIndexRoleItem(); i++) {
                sb.append(rtext.getItemMap().get(startItem.getRoleName()).get(i).getText() + "★");
            }

            CommonUtils.println(out,
                    findObj.getTypeName() + " : " + findObj.getStartItem().getRoleName()
                            + "\n\t" + sb.toString() + "\n\t"
                            + findObj.getMatchStr() + "\t\t"
                            + findObj.getShowStr() + "\n\t"
                            + "(" + findObj.getStartItem().getIndexRoleItem()
                            + " - " + findObj.getStartWords()
                            + ") ~ (" + findObj.getEndItem().getIndexRoleItem()
                            + " - " + findObj.getEndWords() + ")"
            );

            CommonUtils.println(out,"------------------------");
        }
    }

}
