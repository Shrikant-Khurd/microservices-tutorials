package com.userservice.util;

import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;

import com.userservice.dto.ApiResponse;

public class ConstantMethods {
	
	public static ApiResponse successResponse(Object data, String message, HttpStatus... httpStatus) {
		ApiResponse success = new ApiResponse();
		if (httpStatus.length > 0) {
			success.setHttpStatus(httpStatus[0]);
			success.setHttpStatusCode(httpStatus[0].value());
		} else {
			success.setHttpStatus(HttpStatus.OK);
			success.setHttpStatusCode(HttpStatus.OK.value());
		}
		success.setTimeStamp(new Date());
		success.setMessage(message);
		success.setData(data);
		success.setStatus(true);
		return success;
	}

	public static ApiResponse failureResponse(String message, HttpStatus... httpStatus) {
		ApiResponse failure = new ApiResponse();
		if (httpStatus.length > 0) {
			failure.setHttpStatus(httpStatus[0]);
			failure.setHttpStatusCode(httpStatus[0].value());
		} else {
			failure.setHttpStatus(HttpStatus.BAD_REQUEST);
			failure.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
		}
		failure.setTimeStamp(new Date()); 
		failure.setMessage(message);
		failure.setData(null);
		failure.setStatus(false);
		return failure;

	}
	
	public static String decryptUserPassword(String password) {
		
		String secretKey = "1234567890";
		String encryptrdPassword = password;
		String decryptedPassword = "";

		byte[] cipherData = Base64.getDecoder().decode(encryptrdPassword);
		byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secretKey.getBytes(StandardCharsets.UTF_8), md5);
			SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
			IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

			byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
			Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
			aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] decryptedData = aesCBC.doFinal(encrypted);
			decryptedPassword = new String(decryptedData, StandardCharsets.UTF_8);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return decryptedPassword;	
	}
	
	private static byte[][] GenerateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {

	    int digestLength = md.getDigestLength();
	    int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
	    byte[] generatedData = new byte[requiredLength];
	    int generatedLength = 0;

	    try {
	        md.reset();

	        // Repeat process until sufficient data has been generated
	        while (generatedLength < keyLength + ivLength) {

	            // Digest data (last digest if available, password data, salt if available)
	            if (generatedLength > 0)
	                md.update(generatedData, generatedLength - digestLength, digestLength);
	            md.update(password);
	            if (salt != null)
	                md.update(salt, 0, 8);
	            md.digest(generatedData, generatedLength, digestLength);

	            // additional rounds
	            for (int i = 1; i < iterations; i++) {
	                md.update(generatedData, generatedLength, digestLength);
	                md.digest(generatedData, generatedLength, digestLength);
	            }

	            generatedLength += digestLength;
	        }

	        // Copy key and IV into separate byte arrays
	        byte[][] result = new byte[2][];
	        result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
	        if (ivLength > 0)
	            result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);

	        return result;

	    } catch (DigestException e) {
	        throw new RuntimeException(e);

	    } finally {
	        // Clean out temporary data
	        Arrays.fill(generatedData, (byte)0);
	    }
	}
}
