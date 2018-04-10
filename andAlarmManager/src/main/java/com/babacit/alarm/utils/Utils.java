package com.babacit.alarm.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.text.TextUtils;

public class Utils {
	public static byte[] fileToByteArray(String filename) {

		File f = new File(filename);
		if (!f.exists()) {
			System.out.println("=======文件不存在,返回null=========");
			return null;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (bos != null) {
					bos.close();
					bos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getSuffixName(String name) {
		if (!TextUtils.isEmpty(name)) {
			int index = name.lastIndexOf(".");
			if (index > 0) {
				return name.substring(index + 1);
			}
		}
		return null;
	}
}
