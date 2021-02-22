package wang.ulane.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.ulane.file.TraverseFileFromSign;

public class PropLoad {
	
    private static Logger log = LoggerFactory.getLogger(PropLoad.class);  
	
	private static Properties prop = null;

	/**
	 * 初始化方法, 不能使用静态块, 会先于logback加载
	 */
	public static void init(){
		String excutePath = getExcutePath();
		LogReset.initLogback(excutePath);
		init(excutePath);
	}
	
	public static void init(String path){
        Properties prop = new Properties();
        try {
            prop.load(initFile(path, "app.properties"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
            throw new RuntimeException("加载配置文件异常!", e);
		}
        PropLoad.prop = prop;
        log.info("props:"+prop.toString());
	}
	
	public static InputStream initFile(String path, String filename) throws FileNotFoundException{
		InputStream in = null;
		if(path.endsWith(".jar")){
			path = new File(path).getParent();
		}
		File file = new File(path + "/" + filename);
		File file2 = null;
		if(file.exists()){
			log.info("use custom {}", filename);
			in = new FileInputStream(file);
		}else if((file2 = new File(System.getProperty("user.dir") + "/src/main/resources/"+filename)).exists()){
			log.info("use src {}", filename);
			in = new FileInputStream(file2);
		}else{
			log.info("use clasess {}", filename);
			in = PropLoad.class.getClassLoader().getResourceAsStream(filename);
		}
		return in;
	}
	
	public static String getProperty(String key){
		return prop.getProperty(key, null);
	}
	
	public static int getInt(String key){
		return Integer.parseInt(prop.getProperty(key));
	}
	
	public static String getExcutePath(){
		//直接根目录jar包中会报空指针,直接指定META-INF可能会获取到别的jar包中
		URL url = PropLoad.class.getResource("/");
		String path;
		if(url != null){
			//ide或tomcat运行可以获取到
			path = url.getPath();
		}else{
			//jar包运行直接获取到自己的META-INF
			path = TraverseFileFromSign.class.getResource("/META-INF").getPath();
			path = path.substring(5, path.length()-10);
			
//			Enumeration<URL> mis = null;
//			try {
//				mis = Thread.currentThread().getContextClassLoader().getResources("META-INF");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			while(mis.hasMoreElements()){
//				URL mi = mis.nextElement();
//				System.out.println(mi.toString());
////				System.out.println(userDir);
//				if(mi.getPath().startsWith("file:")){
//					rootPath = mi.getPath();
////					break;
//				}
//			}
//			rootPath = rootPath.substring(0, rootPath.length()-8);
		}
		return path;
	}
	
//	public static void main(String[] args) {
//		init(System.getProperty("user.dir"));
//		System.out.println(prop.toString());
//		String bioParams = prop.getProperty("bioParams");
//		JSONObject obj = JSONObject.parseObject(bioParams);
//		System.out.println(obj.toJSONString());
//		System.out.println(obj.getString("GLU"));
//	}
	
}
