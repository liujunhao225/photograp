package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

//import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RsaTest {

	private static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCz+V4K5KmS2TVZ2HhvoMJF5ifr"
			+ "\r"
			+ "z6QTelMnw7BJctqOhyiB8cjHHlf1ceSqMbuwiDaps28PQyDvAk4KDcaKJWbhcwgi"
			+ "\r"
			+ "ss/vft/42fvBzVIu8f4c9FOKfUVWf2ChMczCy12pSFiCZ8PMvnvEIVqZ41djo4RS"
			+ "\r" + "USVdSd2rfLzdGGRMkQIDAQAB" + "\r";
	private static final String DEFAULT_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALP5XgrkqZLZNVnY"
			+ "r"
			+ "eG+gwkXmJ+vPpBN6UyfDsEly2o6HKIHxyMceV/Vx5Koxu7CINqmzbw9DIO8CTgoN"
			+ "r"
			+ "xoolZuFzCCKyz+9+3/jZ+8HNUi7x/hz0U4p9RVZ/YKExzMLLXalIWIJnw8y+e8Qh"
			+ "r"
			+ "WpnjV2OjhFJRJV1J3at8vN0YZEyRAgMBAAECgYB+wxVJ9uS5WeQJ9D5f4Yr0ULet"
			+ "r"
			+ "kAKw9We8ikiQcyUdXnVZkUMpfGXXqvC0L3NTU52syYVf6pg3wBfXXPyKMO3UjyH/"
			+ "r"
			+ "P9MJwp4r/GOsA/ZI3R/zKdTXT+HRe8G/qNg3QU2rEhgCDsixWU67AbBiMz6EG0pO"
			+ "r"
			+ "S1F8r0OpD7jfD8oAAQJBANbBIGq1MgL5l1txUE/yZclqapMnvQ88QhfAhJ5q9g6I"
			+ "r"
			+ "LF18ohTgl0nylmxoMfftEAjsNYDkOkjxppQJI5MT9ZECQQDWijCuDE/ddtlRBY+H"
			+ "r"
			+ "TLmzIcJ5nhYqi94GFoJicgmlSroOmhnJSfaH9+VL/8iIHflOYTilIWn8v98tjLAn"
			+ "r"
			+ "02cBAkEA0hW8EKeRdTeVye7gElWfHZrtB2gom27neTTsh807SJwOSqZSKPpNBho6"
			+ "r"
			+ "7XJA77kJprDs5lpeal18B/Wox4kPAQJBAIqdruZFvjRxXegjl6DryfAbl/PMWLYU"
			+ "r"
			+ "uNdmI8hHtkO+DIjyuEZ+K3Oej+CS25ZXB4XE+zx2YmyT7DcZ95dD2AECQQCS0rt4"
			+ "r"
			+ "31Wn0BsdBt+pII7k5xJzcMU1hIg920eRjXvcBfitVJ+4p6O+4BlPTU8P2TpiBqLu"
			+ "r" + "f1acbjQJS9y7d7aX" + "r";

	private RSAPrivateKey privateKey;

	private RSAPublicKey publicKey;

	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	public RSAPublicKey getPublicKey() {
		return publicKey;
	}

	public void genKeyPair() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
		this.publicKey = (RSAPublicKey) keyPair.getPublic();
	}

	public void loadPublicKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}

	public void loadPublicKey(String publicKeyStr) throws Exception {
		try {
			// BASE64Decoder base64Decoder = new BASE64Decoder();

			byte[] buffer = java.util.Base64.getDecoder().decode(publicKeyStr);
			// byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	public void loadPrivateKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	public void loadPrivateKey(String privateKeyStr) throws Exception {
		try {
			// BASE64Decoder base64Decoder = new BASE64Decoder();

			byte[] buffer = java.util.Base64.getDecoder().decode(privateKeyStr);
			// byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			this.privateKey = (RSAPrivateKey) keyFactory
					.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
			// } catch (NoSuchAlgorithmException e) {
			// throw new Exception("无此加密算法");
			// } catch (NoSuchPaddingException e) {
			// e.printStackTrace();
			// return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
			// } catch (NoSuchAlgorithmException e) {
			// throw new Exception("无此解密算法");
			// } catch (NoSuchPaddingException e) {
			// e.printStackTrace();
			// return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	public static String byteArrayToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i < data.length - 1) {
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		RsaTest rsaEncrypt = new RsaTest();
		// rsaEncrypt.genKeyPair();

		// 加载公钥
		try {
			rsaEncrypt.loadPublicKey(RsaTest.DEFAULT_PUBLIC_KEY);
			System.out.println("加载公钥成功");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("加载公钥失败");
		}

		// 加载私钥
		try {
			rsaEncrypt.loadPrivateKey(RsaTest.DEFAULT_PRIVATE_KEY);
			System.out.println("加载私钥成功");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("加载私钥失败");
		}

		// 测试字符串
		String encryptStr = "Test String chaijunkun";

		try {
			// 加密
			byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(),
					encryptStr.getBytes());
			// 解密
			byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(),
					cipher);
			System.out.println("密文长度:" + cipher.length);
			System.out.println(RsaTest.byteArrayToString(cipher));
			System.out.println("明文长度:" + plainText.length);
			System.out.println(RsaTest.byteArrayToString(plainText));
			System.out.println(new String(plainText));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
