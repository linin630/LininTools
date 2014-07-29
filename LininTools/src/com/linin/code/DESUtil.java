package com.linin.code;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil {
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	public static String encryptDES(String encryptKey, String encryptString) {
		try {
			// IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

			byte[] encryptedData = cipher.doFinal(encryptString
					.getBytes("UTF8"));

			return Base64Util.encode(encryptedData);
		} catch (Exception e) {
			return null;
		}
	}

	public static String decryptDES(String decryptKey, String decryptString) {
		try {
			byte[] byteMi = Base64Util.decode(decryptString);

			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			// IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
			SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);

			return new String(decryptedData, "UTF8");
		} catch (Exception e) {
			return null;
		}
	}
}
