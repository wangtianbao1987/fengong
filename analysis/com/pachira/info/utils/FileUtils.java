package com.pachira.info.utils;

import com.pachira.info.entity.RecogniItem;
import com.pachira.info.entity.RecognizeText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.List;

/**
 * @author: 王添宝
 * @date 2019-11-18 12:02
 */
public class FileUtils {

    public static void copyFile(File source, File target) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            if (!source.exists()) {
                System.out.println("文件不存在：" + source);
                return;
            }
            File toDir = target.getParentFile();
            if (!toDir.isDirectory()) {
                toDir.mkdirs();
            }
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            byte[] buff = new byte[2048];
            int readSize = 0;
            while ((readSize = fis.read(buff)) != -1) {
                fos.write(buff, 0, readSize);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CommonUtils.close(fos, fis);
        }
    }

    public static void wirteSessionsToFile(RecognizeText rtext, File toFile) {
        List<RecogniItem> items = rtext.getItems();
        if (items == null) {
            System.out.println("未合并....");
            return;
        }
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            File toDir = toFile.getParentFile();
            if (!toDir.isDirectory()) {
                toDir.mkdirs();
            }
            fw = new FileWriter(toFile);
            bw = new BufferedWriter(fw);
            for (RecogniItem item : items) {
                bw.write(item.getRoleName() + " - " + item.getText());
                bw.newLine();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CommonUtils.close(bw, fw);
        }
    }

}
