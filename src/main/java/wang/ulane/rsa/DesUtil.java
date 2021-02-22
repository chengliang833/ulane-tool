package wang.ulane.rsa;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 敏感信息加解密
 */
public class DesUtil {
	private static final String key = "rthrgwss";
	
    private static Key o00000(byte[] paramArrayOfByte) throws Exception {
        byte[] arrayOfByte = new byte[8];

        for (int i = 0; (i < paramArrayOfByte.length) && (i < arrayOfByte.length); ++i) {
            arrayOfByte[i] = paramArrayOfByte[i];
        }

        SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte, "DES");

        return localSecretKeySpec;
    }

    private static String byteArr2HexStr(byte[] paramArrayOfByte) throws Exception {
        int i = paramArrayOfByte.length;

        StringBuffer localStringBuffer = new StringBuffer(i * 2);
        for (int j = 0; j < i; ++j) {
            int k = paramArrayOfByte[j];

            while (k < 0) {
                k += 256;
            }

            if (k < 16) {
                localStringBuffer.append("0");
            }
            localStringBuffer.append(Integer.toString(k, 16));
        }
        return localStringBuffer.toString();
    }

    private static byte[] hexStr2ByteArr(String paramString) throws Exception {
        byte[] arrayOfByte1 = paramString.getBytes();
        int i = arrayOfByte1.length;

        byte[] arrayOfByte2 = new byte[i / 2];
        for (int j = 0; j < i; j += 2) {
            String str = new String(arrayOfByte1, j, 2);
            arrayOfByte2[(j / 2)] = (byte) Integer.parseInt(str, 16);
        }
        return arrayOfByte2;
    }

    /**
     * 
     * @Description 方法@param paramArrayOfString
     * @Description 方法@return的实现描述：加密
     * @param @param paramArrayOfString
     * @param @return
     * @return String 
     * @throws 
     * @date  2017年7月12日 下午12:54:16
     */
    public static String desEncode(String param) {

        try {
            Key localKey = o00000(key.getBytes());
            Cipher localCipher = Cipher.getInstance("DES");
            localCipher.init(1, localKey);
            return byteArr2HexStr(localCipher.doFinal(param.getBytes()));
        } catch (Exception localException) {
        }
        return param;

    }

    /**
     * 
     * @Description 方法@param paramArrayOfString
     * @Description 方法@return的实现描述：解密 
     * @param @param paramArrayOfString
     * @param @return
     * @return String 
     * @throws 
     * @date  2017年7月12日 下午12:54:36
     */
    public static String desDecode(String param) {
        try {
            Key localKey = o00000(key.getBytes());

            Cipher localCipher = Cipher.getInstance("DES");
            localCipher.init(2, localKey);
            return new String(localCipher.doFinal(hexStr2ByteArr(param)));
        } catch (Exception localException) {
        }
        return param;
    }
    
    /**
     * md5校验修改
     */
    public static String md5(String text){
    	return DigestUtils.md5Hex(text);
    }

    
    public static void main(String[] args) {
    	
	}
    
}
