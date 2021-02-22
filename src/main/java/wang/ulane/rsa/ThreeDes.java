package wang.ulane.rsa;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import wang.ulane.file.ConvertUtil;

public class ThreeDes {
	
	private static final String key = "dsf7dfFds4fdsSf45fF3DSfF";
	private static final byte[] keyiv = "12345678".getBytes();
	private static final String algorithm = "DESede";
	private static final String ECB = "/ECB/PKCS5Padding";
	private static final String CBC = "/CBC/PKCS5Padding"; 
	
//	static {
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//	}
	
//	public final static byte[] keyBytes = { 0x11, (byte) 0x88,...}; // 24字节的密钥
	
    /**
     * ECB加密,不要IV
     * @param key 密钥
     * @param data 明文
     * @return Base64编码的密文
     * @throws UnsupportedEncodingException 
     * @throws Exception
     */
	public static String des3EncodeECB(String data) throws Exception{
		return des3EncodeECB(key, data);
	}
	public static String des3EncodeECB(String key, String data) throws Exception{
		return ConvertUtil.toBase64(des3EncodeECB(key.getBytes("utf-8"), data.getBytes("utf-8")));
	}
    public static byte[] des3EncodeECB(byte[] key, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(algorithm);
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(algorithm + ECB);
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
    /**
     * ECB解密,不要IV
     * @param key 密钥
     * @param data Base64编码的密文
     * @return 明文
     * @throws  
     * @throws Exception
     */
    public static String des3DecodeECB(String data) throws Exception{
    	return des3DecodeECB(key, data);
    }
    public static String des3DecodeECB(String key, String data) throws Exception{
    	return new String(des3DecodeECB(key.getBytes("utf-8"), ConvertUtil.decodeBase64ToBytes(data)));
    }
    public static byte[] des3DecodeECB(byte[] key, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(algorithm);
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(algorithm + ECB);
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
    /**
     * CBC加密
     * @param key 密钥
     * @param keyiv IV
     * @param data 明文
     * @return Base64编码的密文
     * @throws  
     * @throws Exception
     */
    public static String des3EncodeCBC(String data) throws Exception{
    	return des3EncodeCBC(key, keyiv, data);
    }
    public static String des3EncodeCBC(String key, byte[] keyiv, String data) throws Exception{
    	return ConvertUtil.toBase64(des3EncodeCBC(key.getBytes("utf-8"), keyiv, data.getBytes("utf-8")));
    }
    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(algorithm);
        deskey = keyfactory.generateSecret(spec);
//        Cipher cipher = Cipher.getInstance(algorithm + CBC, "BC");
        Cipher cipher = Cipher.getInstance(algorithm + CBC);
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
    /**
     * CBC解密
     * @param key 密钥
     * @param keyiv IV
     * @param data Base64编码的密文
     * @return 明文
     * @throws  
     * @throws Exception
     */
    public static String des3DecodeCBC(String data) throws Exception{
    	return des3DecodeCBC(key, keyiv, data);
    }
    public static String des3DecodeCBC(String key, byte[] keyiv, String data) throws Exception{
    	return new String(des3DecodeCBC(key.getBytes("utf-8"), keyiv, ConvertUtil.decodeBase64ToBytes(data)));
    }
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(algorithm);
        deskey = keyfactory.generateSecret(spec);
//        Cipher cipher = Cipher.getInstance(algorithm + CBC, "BC");
        Cipher cipher = Cipher.getInstance(algorithm + CBC);
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
    
    

    public static void main(String[] args) throws Exception {
//        byte[] key = new BASE64Decoder().decodeBuffer("");
//    	String key = "";
//        byte[] keyiv = {1, 2, 3, 4, 5, 6, 7, 8};
        
        String data = "";
        
//        for(byte a:keyiv){
//        	System.out.println(a);
//        }
        
//        System.out.println("ECB加密解密");
//        String str3 = des3EncodeECB(data);
//        String str4 = des3DecodeECB(str3);
//        System.out.println(str3);
//        System.out.println(str4);
//        System.out.println(new BASE64Encoder().encode(str3));
//        System.out.println(new String(str4, "UTF-8"));
//        System.out.println("-----------------------------");
        System.out.println("CBC加密解密");
//        String str5 = des3EncodeCBC(data);
//        String str6 = des3DecodeCBC(str5);
////        System.out.println(new BASE64Encoder().encode(str5));
////        System.out.println(new String(str6, "UTF-8"));4
//        System.out.println(str5);
//        System.out.println(str6);
        
    }
    
}
