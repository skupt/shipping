package rozaryonov.shipping.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
	
	private PasswordEncoder(){}
	
	public static String getHash(String pass) {
		if (pass==null) return "";
		String encrypted = pass;
		MessageDigest messageDigest = null;
		byte[] bytesEncoded = null;
		try {
		messageDigest = MessageDigest.getInstance("SHA-1"); // only once !
		messageDigest.update(encrypted.getBytes(StandardCharsets.UTF_8));
		bytesEncoded = messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		}
		BigInteger bigInt = new BigInteger(1, bytesEncoded); //1(sign+) or -1(sign-)
		return bigInt.toString(16);
	}
}
