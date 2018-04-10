package com.babacit.alarm.utils;

public class NumberUtils {
	public static String changeNumericToString(int num) {
		String _num = "";
		switch (num) {
		case 0:
			_num = "周一";
			break;
		case 1:
			_num = "周二";
			break;
		case 2:
			_num = "周三";
			break;
		case 3:
			_num = "周四";
			break;
		case 4:
			_num = "周五";
			break;
		case 5:
			_num = "周六";
			break;
		case 6:
			_num = "周日";
			break;
		default:
			break;
		}
		return _num;
	}

	public static int[] stringArrToNumericArr(String[] src) {
		int[] dest = new int[] { -1, -1, -1, -1, -1, -1, -1 };
		for (int i = 0; i < src.length; i++) {
			switch (Integer.parseInt(src[i].trim())) {
			case 0:
				dest[i] = 0;
				break;
			case 1:
				dest[i] = 1;
				break;
			case 2:
				dest[i] = 2;
				break;
			case 3:
				dest[i] = 3;
				break;
			case 4:
				dest[i] = 4;
				break;
			case 5:
				dest[i] = 5;
				break;
			case 6:
				dest[i] = 6;
				break;
			default:
				break;
			}
		}
		return dest;
	}

	public static String number2Character(int i) {
		String character = "";
		switch (i) {
		case 1:
			character = "a";
			break;
		case 2:
			character = "b";
			break;
		case 3:
			character = "c";
			break;
		case 4:
			character = "d";
			break;
		case 5:
			character = "e";
			break;
		case 6:
			character = "f";
			break;
		case 7:
			character = "g";
			break;
		case 8:
			character = "h";
			break;
		case 9:
			character = "i";
			break;
		case 10:
			character = "j";
			break;
		case 11:
			character = "k";
			break;
		case 12:
			character = "l";
			break;
		}
		return character;
	}

	public static String weekStr(String week) {
		String str = "";
		if (week.contains("一")) {
			str = "0";
		} else if (week.contains("二")) {
			str = "1";
		} else if (week.contains("三")) {
			str = "2";
		} else if (week.contains("四")) {
			str = "3";
		} else if (week.contains("五")) {
			str = "4";
		} else if (week.contains("六")) {
			str = "5";
		} else if (week.contains("日")) {
			str = "6";
		}
		return str;
	}
}
