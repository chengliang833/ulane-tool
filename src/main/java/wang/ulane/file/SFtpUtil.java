package wang.ulane.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFtpUtil {

	private static final  Logger LOG = LoggerFactory.getLogger(SFtpUtil.class);
	
	private ChannelSftp sftp;  
    private Session session;  
    private String username;  
    private String password;   
    private String host;  
    private int port;  
	
    public SFtpUtil(String username, String password, String host, int port) {  
        this.username = username;  
        this.password = password;  
        this.host = host;  
        this.port = port;  
    }  

    public void login(){  
        try {  
            JSch jsch = new JSch();  
            session = jsch.getSession(username, host, port);  
            LOG.info("Session is build");  
            if (password != null) {  
                session.setPassword(password);    
            }  
            Properties config = new Properties();  
            config.put("StrictHostKeyChecking", "no");  
                
            session.setConfig(config);  
            session.connect();  
              
            Channel channel = session.openChannel("sftp");  
            channel.connect();  
    
            sftp = (ChannelSftp) channel;  
            LOG.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));  
        } catch (JSchException e) {  
        	LOG.error("Cannot connect to sftp server : {}:{} \n message is: {}", new Object[]{host, port, e.getMessage()});    
        }  
    }    
    
    public void logout(){  
        if (sftp != null) {  
            if (sftp.isConnected()) {  
                sftp.disconnect();  
            }  
        }  
        if (session != null) {  
            if (session.isConnected()) {  
                session.disconnect();  
            }  
        }  
    }  
    
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException{  
    	String[] folders = directory.split( "/" );  
    	for (String folder : folders) {  
    	    if (folder.length() > 0) {  
    	        try {  
    	            sftp.cd(folder);  
    	        }  
    	        catch (SftpException e) {  
    	        	LOG.warn("directory is not exist");  
    	            sftp.mkdir( folder );  
    	            sftp.cd( folder );  
    	        }  
    	    }  
    	}  
        sftp.put(input, sftpFileName);  
    }  
    
    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException{  
        File file = new File(uploadFile);  
        upload(directory, file.getName(), new FileInputStream(file));  
    }
    
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException{  
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));  
    }  
    
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException{    
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));    
    }  
    
    public static void main(String[] args) throws SftpException, IOException {  
        SFtpUtil sftp = new SFtpUtil("test", "123456", "192.168.1.1", 22);  
        sftp.login();  
        File file = new File("test.csv");  
        InputStream is = new FileInputStream(file);  
        sftp.upload("/data/work", "test_sftp_upload.csv", is);  
        sftp.logout();  
    }  
}  
