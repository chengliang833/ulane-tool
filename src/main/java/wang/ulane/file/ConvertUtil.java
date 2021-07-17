package wang.ulane.file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertUtil {
	
    private static Logger log = LoggerFactory.getLogger(ConvertUtil.class);  
	
	// inputStream转outputStream
	public static ByteArrayOutputStream parse(InputStream input) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		int len = 0;
		byte[] b = new byte[10240];
		while ((len = input.read(b, 0, b.length)) != -1) {
			swapStream.write(b, 0, len);
		}
		return swapStream;
	}

	public static ByteArrayOutputStream parseWithStrWrap(String fromStr, String toStr, InputStream input) throws IOException {
		String str = parseString(input);
		
		if(!Pattern.compile(".*"+fromStr+".*", Pattern.DOTALL).matcher(str).matches()){
			return null;
		}
		String str2 = str.replaceAll(fromStr, toStr);
		return parseOutputStream(str2);
	}
	
	public static ByteArrayOutputStream parseWithHexWrap(String fromHex, String toHex, InputStream input) throws IOException {
		String hexStr = encodeHexStr(input);
		if(!hexStr.matches(".*"+fromHex+".*")){
			return null;
		}
		String hexStr2 = hexStr.replaceAll(fromHex, toHex);
		try {
			return decodeHexStrToOutput(hexStr2);
		} catch (DecoderException e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	// outputStream转inputStream
	public static ByteArrayInputStream parse(ByteArrayOutputStream output) {
		byte[] data = streamToByte(output);
		ByteArrayInputStream swapStream = new ByteArrayInputStream(data);
		return swapStream;
	}
	
	public static byte[] streamToByte(InputStream input) throws IOException {
		int inputLen = input.available();
		if(inputLen == 0){
			byte[] data = null;
			try (ByteArrayOutputStream baos = parse(input)) {
				data = baos.toByteArray();
			}
			return data;
		}
		
		int len = 0;
		int allLen = 0;
		int stepLen = inputLen;
		if(stepLen > 10240){
			stepLen = 10240;
		}
		
		byte[] buf = new byte[inputLen];
		while (allLen < inputLen) {
			len = input.read(buf, allLen, stepLen);
			if(len == -1){
				break;
			}
			allLen += len;
		}
		return buf;
	}
	
	public static byte[] streamToByte(ByteArrayOutputStream output) {
		return output.toByteArray();
	}

	// inputStream转String
	public static String parseString(InputStream input) throws IOException {
		return parseString(input, "utf-8");
	}
	
	// inputStream转String去掉注释
	public static String parseStringWithoutComment(InputStream input) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String brRead = null;
		int largeComment = 0;
		while((brRead = br.readLine()) != null){
			brRead = brRead.replaceAll("//.*", "");
			if(brRead.contains("/*") && brRead.contains("*/")){
				brRead = brRead.replaceAll("/\\*.*\\*/", "") + "\n";
			}else if(brRead.contains("/*")){
				brRead = brRead.replaceAll("/\\*.*", "") + "\n";
				largeComment++;
			}else if(brRead.contains("*/")){
				brRead = brRead.replaceAll(".*\\*/", "") + "\n";
				largeComment--;
			}else if(largeComment == 0){
				// /*和*/都没有才看largeComment
				//保持原值
				brRead = brRead + "\n";
			}else{
				brRead = "";
			}
			sb.append(brRead);
		}
		return sb.toString();
	}
	
	// inputStream转String
	public static String parseString(InputStream input, String charSet) throws IOException {
		String result = null;
//		byte[] data = streamToByte(input);
//		result = new String(data, charSet);
//		return result;
		
//		return parse(input).toString(charSet);
		
		return new String(streamToByte(input), charSet);
	}

	// OutputStream转String
	public static String parseString(ByteArrayOutputStream output) {
		String result = null;
//		byte[] data = streamToByte(output);
		try {
			result = output.toString("utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	// String转inputStream
	public static ByteArrayInputStream parseInputStream(String in) {
		try {
			return parseInputStream(in.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static ByteArrayInputStream parseInputStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

	// String转outputStream
	public static ByteArrayOutputStream parseOutputStream(String in) throws IOException {
		return parse(parseInputStream(in));
	}
	public static ByteArrayOutputStream parseOutputStream(byte[] bytes) throws IOException {
		return parse(parseInputStream(bytes));
	}
	
	
	
	
	
	
	
	
	
	public static String toBase64(String in) throws UnsupportedEncodingException {
		return toBase64(in.getBytes("UTF-8"));
	}
	
	public static String toBase64(byte[] in) throws UnsupportedEncodingException {
		byte[] data = Base64.encodeBase64(in);
		return new String(data, "UTF-8");
	}
	
	public static String decodeBase64(String base64String) throws UnsupportedEncodingException {
		return new String(decodeBase64ToBytes(base64String));
	}
	
	public static byte[] decodeBase64ToBytes(String base64String) throws UnsupportedEncodingException {
		return decodeBase64ToBytes(base64String.getBytes("UTF-8"));
	}
	
	public static String decodeBase64(byte[] in) throws UnsupportedEncodingException {
		return new String(decodeBase64ToBytes(in));
	}
	
	public static byte[] decodeBase64ToBytes(byte[] in) throws UnsupportedEncodingException {
		return Base64.decodeBase64(in);
	}
	
	public static void toOutputFromBase64(String base64String, OutputStream output) throws UnsupportedEncodingException, IOException {
		output.write(decodeBase64ToBytes(base64String));
	}
	
	public static String encodeHexStr(byte[] inbytes){
		return Hex.encodeHexString(inbytes);
	}
	
	public static String encodeHexStr(byte[] inbytes, boolean toLowerCase){
		return new String(Hex.encodeHex(inbytes, toLowerCase));
	}
	
	public static String encodeHexStr(InputStream input) throws IOException {
		return encodeHexStr(streamToByte(input));
	}

	public static String encodeHexStr(InputStream input, boolean toLowerCase) throws IOException {
		return encodeHexStr(streamToByte(input), toLowerCase);
	}
	
	public static byte[] decodeHexStr(String hexStr) throws DecoderException{
		return Hex.decodeHex(hexStr.toCharArray());
	}

	public static ByteArrayOutputStream decodeHexStrToOutput(String hexStr) throws IOException, DecoderException {
		return parse(parseInputStream(decodeHexStr(hexStr)));
	}
	
	public static ByteArrayInputStream decodeHexStrToInput(String hexStr) throws IOException, DecoderException {
		return parseInputStream(decodeHexStr(hexStr));
	}
	
}