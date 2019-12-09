package com.pchira.fengong.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import com.pachira.info.utils.CommonUtils;

public class FileUtils {
	private static final int buffSize = 1024 * 4;
	public static void copyFile(File sourceFile, OutputStream out) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(sourceFile);
			byte[] buff = new byte[buffSize];
			int len = 0;
			while ((len=fis.read(buff)) != -1) {
				out.write(buff, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.close(fis);
		}
	}
}
