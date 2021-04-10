package wang.ulane.ftp;

import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

public class MyFtpServer {
	
	public static void main(String[] args) throws FtpException {
//		args = new String[]{"D:\\Download\\D__Temp","211.159.185.18","27922"};
		DataConnectionConfigurationFactory dataFac = new DataConnectionConfigurationFactory();
		ListenerFactory factory = new ListenerFactory();
		factory.setPort(2121);
		
		ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
		connectionConfigFactory.setAnonymousLoginEnabled(true);
		
		BaseUser user = new BaseUser();
//		user.setName("anonymous");
		user.setName("admin");
		user.setPassword("youlan-121");
		switch (args.length) {
			case 0: 
				user.setHomeDirectory("E:\\");
				break;
			case 3:
				dataFac.setPassivePorts(args[2]);
			case 2:
				dataFac.setPassiveExternalAddress(args[1]);
//				System.out.println(dataFac.getActiveLocalPort());
//				System.out.println(dataFac.getPassivePorts());
			default:
				user.setHomeDirectory(args[0]);
		}
		List authorities = new ArrayList();
		authorities.add(new WritePermission());
		user.setAuthorities(authorities);
		factory.setDataConnectionConfiguration(dataFac.createDataConnectionConfiguration());
		FtpServerFactory serverFactory = new FtpServerFactory();
		serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());
		serverFactory.addListener("default", factory.createListener());
		serverFactory.getUserManager().save(user);
		FtpServer server = serverFactory.createServer();
		server.start();
		System.out.println("启动成功!");
	}
}
