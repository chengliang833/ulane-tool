package wang.ulane.rsa;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;

public class RSAUtil {

	//产生RSA密钥
	public static JSONObject getServerRSA(){
		JSONObject jsonObj = new JSONObject();
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(1024);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			
			jsonObj.put("pubKey", new String(Base64.encodeBase64(publicKey.getEncoded())));
			jsonObj.put("priKey", new String(Base64.encodeBase64(privateKey.getEncoded())));
			
//			System.out.println("serverpubpriKey:"+jsonObj.toString());
			System.out.println("pub:"+jsonObj.getString("pubKey"));
			System.out.println("pri:"+jsonObj.getString("priKey"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
	
	//RSA加密
	public static String RSAEncode(String text, String pubKey){
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(pubKey));
	        KeyFactory factory = KeyFactory.getInstance("RSA");
	        PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeBase64String(cipher.doFinal(text.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//RSA解密
	public String RSADecode(String base64Message,String base64PrivateKey) {
        String encryptValue = null;
		try {
            byte decodeMessageBytes[] = Base64.decodeBase64(base64Message);
            byte[] keyBytes = Base64.decodeBase64(base64PrivateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);   
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");   
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);   
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            encryptValue = new String(cipher.doFinal(decodeMessageBytes));
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        return encryptValue;
    }
	
	//超长RSA加密
	public static String encryptRSALong(String text, String publicKey){
		try {
	    	byte[] data = text.getBytes();
	        byte[] keyBytes = Base64.decodeBase64(publicKey);  
	        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
	        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);  
	        // 对数据加密  
	        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
	        cipher.init(Cipher.ENCRYPT_MODE, publicK);  
	        int inputLen = data.length;  
	        ByteArrayOutputStream out = new ByteArrayOutputStream();  
	        int offSet = 0;  
	        byte[] cache;  
	        int i = 0;  
	        // 对数据分段加密  
	        while (inputLen - offSet > 0) {  
	            if (inputLen - offSet > 117) {  
	                cache = cipher.doFinal(data, offSet, 117);  
	            } else {  
	                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
	            }  
	            out.write(cache, 0, cache.length);  
	            i++;  
	            offSet = i * 117;  
	        }  
	        byte[] encryptedData = out.toByteArray();
	        out.close();  
	        return Base64.encodeBase64String(encryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
	
	//超长RSA解密
    public static String decryptRSALong(String text, String privateKey) {
    	try {
        	byte[] encryptedData = Base64.decodeBase64(text);
            byte[] keyBytes = Base64.decodeBase64(privateKey);  
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
            cipher.init(Cipher.DECRYPT_MODE, privateK);  
            int inputLen = encryptedData.length;  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            int offSet = 0;  
            byte[] cache;  
            int i = 0;  
            // 对数据分段解密  
            while (inputLen - offSet > 0) {  
                if (inputLen - offSet > 128) {  
                    cache = cipher.doFinal(encryptedData, offSet, 128);  
                } else {  
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
                }  
                out.write(cache, 0, cache.length);  
                i++;  
                offSet = i * 128;
            }  
            byte[] decryptedData = out.toByteArray();  
            out.close();  
            return new String(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
	
}
