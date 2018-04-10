package com.babacit.alarm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;

import android.text.TextUtils;

public class DesUtils {
	private static String strDefaultKey = "cengage";
	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	public DesUtils() throws Exception {
		this(strDefaultKey);
	}

	public DesUtils(String strKey) {
		// Security.addProvider(null);
		Key key;
		try {
			key = getKey(strKey.getBytes());
			encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);
			decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] encrypt(byte[] arrB) throws Exception {
		return encryptCipher.doFinal(arrB);
	}

	public String encrypt(String strIn) throws Exception {
		return byteArr2HexStr(encrypt(strIn.getBytes()));
	}

	public byte[] decrypt(byte[] arrB) throws Exception {
		return decryptCipher.doFinal(arrB);
	}

	public String decrypt(String strIn) throws Exception {
		return new String(decrypt(hexStr2ByteArr(strIn)));
	}

	private Key getKey(byte[] arrBTmp) throws Exception {
		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[8];
		// 将原始字节数组转换为8位
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		// 生成密钥
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
		return key;
	}

	/**
	 * md5加密
	 * 
	 * @param s
	 * @return
	 */
	public final static String MD5(String s) {
		if (!TextUtils.isEmpty(s)) {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', 'A', 'B', 'C', 'D', 'E', 'F' };
			try {
				byte[] btInput = s.getBytes();
				// 获得MD5摘要算法的 MessageDigest 对象
				MessageDigest mdInst = MessageDigest.getInstance("MD5");
				// 使用指定的字节更新摘要
				mdInst.update(btInput);
				// 获得密文
				byte[] md = mdInst.digest();
				// 把密文转换成十六进制的字符串形式
				int j = md.length;
				char str[] = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					str[k++] = hexDigits[byte0 >>> 4 & 0xf];
					str[k++] = hexDigits[byte0 & 0xf];
				}
				return new String(str);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return "";
		}
	}

	/**
	 * md5加密
	 * 
	 * @param s
	 * @return
	 */
	public final static byte[] MD5(byte[] btInput) {
		// char
		// hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			return md;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 把指定的字节数组用gzip压缩
	 * 
	 * @param src
	 * @return 压缩过的字节数组
	 */
	public static byte[] gzip(byte[] src) {
		if (null != src) {
			final ByteArrayOutputStream os = new ByteArrayOutputStream(
					src.length);
			try {
				final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(
						os);
				gzipOutputStream.write(src);
				gzipOutputStream.flush();
				gzipOutputStream.close();
				return os.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 把指定的字节数组用gzip解压缩
	 * 
	 * @param src
	 * @return 解压缩过的字节数组
	 */
	public static byte[] ungzip(byte[] src) {
		if (null != src) {
			final ByteArrayInputStream is = new ByteArrayInputStream(src);
			final ByteArrayOutputStream os = new ByteArrayOutputStream(
					src.length);
			try {
				final GZIPInputStream in = new GZIPInputStream(is);
				final byte[] buffer = new byte[512];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					os.write(buffer, 0, len);
					os.flush();
				}
				in.close();
				return os.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public static byte[] transEncode(String res) {
		if (TextUtils.isEmpty(res)) {
			return null;
		}
		byte[] bsInput = res.getBytes();
		byte[] gzipInput = gzip(bsInput);
		byte[] bsOuput = new byte[gzipInput.length + 20];
		bsOuput[0] = (byte) 0xca;
		bsOuput[1] = (byte) 0xfe;
		bsOuput[2] = (byte) 0xba;
		bsOuput[3] = (byte) 0xbe;
		byte[] bsInputMd5 = MD5(gzipInput);
		for (int i = 0; i < bsInputMd5.length; i++) {
			bsOuput[i + 4] = bsInputMd5[i];
		}
		for (int i = 0; i < gzipInput.length; i++) {
			bsOuput[i + 20] = gzipInput[i];
		}
		return bsOuput;
	}

	public static String transDecode(byte[] bsInput) {
		if (bsInput.length > 20) {
			byte[] bsMd5 = new byte[16];
			for (int i = 0; i < 16; i++) {
				bsMd5[i] = bsInput[i + 4];
			}
			byte[] bsRes = new byte[bsInput.length - 20];
			for (int i = 0; i < bsRes.length; i++) {
				bsRes[i] = bsInput[i + 20];
			}
			byte[] bsResMd5 = MD5(bsRes);
			for (int i = 0; i < 16; i++) {
				if (bsMd5[i] != bsResMd5[i]) {
					return null;
				}
			}

			byte[] unGzipRes = ungzip(bsRes);
			return new String(unGzipRes);
		}
		return null;
	}
}
