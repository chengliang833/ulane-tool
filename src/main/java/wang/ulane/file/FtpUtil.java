package wang.ulane.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class FtpUtil {

	
	public static void main(String[] args) {
		System.out.println("start");
		try {
//			boolean a = upload("10.22.60.8",21,"core","core","g:/file/fos.txt","/app/mcv7dev/apps/onl/temp","ftptest.txt");
//			boolean a = upload("39.108.98.228",21,"root","YouLanYiXuan-121","g:/file/abc.txt","/var/ftp/ftpfile/test","ftptest1.txt");
//			boolean a = uploadFile("39.108.98.228",21,"root","YouLanYiXuan-121",new FileInputStream(new File("g:/file/abc.txt")),"/var/ftp/ftpfile","abc.txt");
//			boolean a = uploadFile("10.22.60.8",21,"core","core",new FileInputStream(new File("g:/file/abc.txt")),"/app","ftptest.txt");
//			boolean a = uploadDataStream("39.108.98.228",21,"root","YouLanYiXuan-121","test第三方的是Data\nte多福多寿st","/var/ftp/ftpfile/te飞碟说st3","ftp分递四方速递test.txt");
			boolean a = uploadDataStream("10.22.60.8",21,"ftpuser","ftpuser","testData\ntest","/app/ftp/cimbcbs/gift_upload","ftptest.txt");
			System.out.println(a);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static boolean uploadFile(String host, int port, String username, String password, InputStream input, String filePath, String filename) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();
        //打开调试信息
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        try {  
            int reply;  
            ftp.connect(host, port);// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器  
            ftp.login(username, password);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            //切换到上传目录  
            if (!ftp.changeWorkingDirectory(filePath)) {  
                //如果目录不存在创建目录  
                String[] dirs = filePath.split("/");  
                String tempPath = "";  
                for (String dir : dirs) {  
                    if (null == dir || "".equals(dir)) continue;  
                    tempPath += "/" + dir;  
                    if (!ftp.changeWorkingDirectory(tempPath)) {  
                        if (!ftp.makeDirectory(tempPath)) {  
                            return result;  
                        } else {  
                            ftp.changeWorkingDirectory(tempPath);  
                        }  
                    }  
                }  
            }  
            //设置上传文件的类型为二进制类型  
            ftp.setFileType(FTP.BINARY_FILE_TYPE);  
            //上传文件  
            result = ftp.storeFile(filename, input);
            input.close();  
            ftp.logout();  
            result = true;  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    }

	/**
	 * ftp上传单个文件
	 * 
	 * @param ftpUrl ftp地址
	 * @param port ftp端口地址
	 * @param userName ftp的用户名
	 * @param password ftp的密码
	 * @param srcFileName 要上传的文件全路径名
	 * @param directory 上传至ftp的路径名不包括ftp地址
	 * @param destName 上传至ftp后存储的文件名
	 * @throws IOException
	 */
	public static boolean upload( String ftpUrl,  int port,String userName, String password,
			 String srcFileName,String directory, String destName) throws IOException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        FileInputStream fis = null;
		boolean result = false;
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();

            //如果目录不存在创建目录  
            if (!ftpClient.changeWorkingDirectory(directory)) {  
                String[] dirs = directory.split("/");  
                String tempPath = "";  
                for (String dir : dirs) {  
                    if (null == dir || "".equals(dir)) continue;  
                    tempPath += "/" + dir;  
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {  
                        if (!ftpClient.makeDirectory(tempPath)) {  
                            return result;  
                        } else {  
                            ftpClient.changeWorkingDirectory(tempPath);  
                        }  
                    }  
                }  
            }
			
			File srcFile = new File(srcFileName);
			fis = new FileInputStream(srcFile);
			// 设置上传目录
			ftpClient.changeWorkingDirectory(directory);
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			result = ftpClient.storeFile(destName, fis);
			return result;
		} catch (NumberFormatException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}
	
	/**
	 * ftp上传流数据
	 * 
	 * @param ftpUrl ftp地址
	 * @param port ftp端口地址
	 * @param userName ftp的用户名
	 * @param password ftp的密码
	 * @param fileData 要上传的文件数据
	 * @param directory 上传至ftp的路径名不包括ftp地址
	 * @param destName 上传至ftp后存储的文件名
	 * @throws IOException
	 */
	public static boolean uploadDataStream( String ftpUrl, int port, String userName, String password, String fileData, String directory, String destName) throws IOException {
		directory = new String(directory.getBytes(), "ISO-8859-1");
		destName = new String(destName.getBytes(), "ISO-8859-1");
		
		FTPClient ftpClient = new FTPClient();
        //打开调试信息
		ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		boolean result = false;
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();

			// 设置上传目录
//			ftpClient.changeWorkingDirectory(directory);
            //如果目录不存在创建目录  
            if (!ftpClient.changeWorkingDirectory(directory)) {  
                String[] dirs = directory.split("/");  
                String tempPath = "";  
                for (String dir : dirs) {  
                    if (null == dir || "".equals(dir)) continue;  
                    tempPath += "/" + dir;  
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {  
                        if (!ftpClient.makeDirectory(tempPath)) {  
                            return result;  
                        } else {  
                            ftpClient.changeWorkingDirectory(tempPath);  
                        }  
                    }  
                }  
            }
            
			ftpClient.setBufferSize(1024);
//			ftpClient.setControlEncoding("GBK");
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			result = ftpClient.storeFile(destName, new ByteArrayInputStream(fileData.getBytes()));
			return result;
		} catch (NumberFormatException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}
	
	
}