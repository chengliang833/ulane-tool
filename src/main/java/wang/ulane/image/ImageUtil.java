package wang.ulane.image;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

public class ImageUtil {

	/**
	 * 将图片转换成Base64编码
	 * 
	 * @param imgFilePath
	 *            待处理图片
	 * @return
	 */
	public static String getImgStr(String imgFilePath) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(data));
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成图片
	 * 
	 * @param imgStr
	 *            图片数据
	 * @param imgFilePath
	 *            保存图片全路径地址
	 * @return
	 */
	public static boolean generateImage(String imgStr, String imgFilePath) {
		//
		if (imgStr == null) // 图像数据为空
			return false;

		try {
			// Base64解码
			byte[] b = Base64.decodeBase64(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片

			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
