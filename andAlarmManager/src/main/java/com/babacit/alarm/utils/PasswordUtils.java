package com.babacit.alarm.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtils {
	public static boolean isValid(String str) {
		boolean b = false;
		Pattern pt = Pattern.compile("\\w{8,12}");
		Matcher mt = pt.matcher(str);
		
		if (mt.matches()) {
			b = true;
		}
		return b;
	}
}
