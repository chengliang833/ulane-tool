package wang.ulane.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

public class FileManage {
	
	public static BufferedInputStream loadInputStream(String path) throws FileNotFoundException{
		return new BufferedInputStream(new FileInputStream(path));
	}
	
	public static void saveFile(String path, String str) throws IOException {
//		saveFile(path, str.getBytes("utf-8"));
		FileUtils.writeStringToFile(new File(path), str, "utf-8");
	}
	
	public static void saveFile(String path, byte[] bytes) throws IOException {
//		ByteArrayInputStream bais = ConvertUtil.parseInputStream(bytes);
//		saveFile(path, bais);
		FileUtils.writeByteArrayToFile(new File(path), bytes);
	}
	
	public static void saveFile(String path, InputStream is) throws IOException{
		File file = new File(path);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
    	int len = 0;
		byte[] bt = new byte[10240];
        while ((len = is.read(bt)) != -1) {
        	bos.write(bt,0,len);
        }
//        bos.flush();
        is.close();
        bos.close();
	}
	
    public static void saveFile(String path, ByteArrayOutputStream baos) throws IOException{
    	FileOutputStream fos = new FileOutputStream(path);
    	baos.writeTo(fos);
    	baos.close();
    	fos.close();
    }
}
