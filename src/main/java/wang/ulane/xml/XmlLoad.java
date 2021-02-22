package wang.ulane.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import wang.ulane.file.ConvertUtil;
import wang.ulane.file.FileManage;
import wang.ulane.init.PropLoad;

public class XmlLoad {

    private static Logger log = LoggerFactory.getLogger(XmlLoad.class);  
    
	public static Map<String, Map<String, String>> load(InputStream is) {
		Document doc = null;
		try {
			SAXReader sax = new SAXReader();
			doc = sax.read(is);
//			String xmiStr = ConvertUtil.parseString(is);
//			doc = DocumentHelper.parseText(xmiStr);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("sql文件加载错误");
		}
		
		Map<String, Map<String, String>> map = new HashMap<>();
		List<Node> nodes = doc.selectNodes("//file");
		for(Node node:nodes){
			Element ele = (Element)node;
			String fileName = ele.attributeValue("id");
			if(map.get(fileName) == null){
				map.put(fileName, new HashMap<>());
			}
			List<Node> subNodes = ele.selectNodes("sql");
			for(Node subNode:subNodes){
				Element subEle = (Element)subNode;
				map.get(fileName).put(subEle.attributeValue("id"), subEle.getTextTrim());
			}
		}
		return map;
	}

	public static Map<String, Map<String, String>> loadAll(String path, String filename) {
		JSONArray arr = null;
		try {
			arr = JSON.parseArray(ConvertUtil.parseString(PropLoad.initFile(path, filename)));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("xml文件列表加载错误");
		}
		Map<String, Map<String, String>> map = new HashMap<>();
		for(Object obj:arr){
			String fileRelatePath = (String) obj;
//			System.out.println("/sql"+fileRelatePath);
			map.putAll(load(XmlLoad.class.getResourceAsStream("/sql"+fileRelatePath)));
		}
		return map;
	}




	public static void autoList(String resourcePath){
		String devPath = System.getProperty("user.dir")+"/src/main/resources";
		File folder = new File(devPath + resourcePath);
		List<String> filePaths = new ArrayList<String>();
		recordFileRelatePath(folder, folder.getPath(), filePaths);
		StringBuilder sb = new StringBuilder("[\r\n");
		for(String filePath:filePaths){
			sb.append("\"").append(filePath.replace("\\", "/")).append("\",\r\n");
		}
		sb.append("]");
		try {
			FileManage.saveFile(devPath+"/autoList.json", sb.toString());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		System.out.println("auto finish...");
	}
	public static void recordFileRelatePath(File file, String resourcePath, List<String> list){
		if(file.isFile()){
			list.add(file.getPath().replace(resourcePath, ""));
		}else{
			for(File subFile:file.listFiles()){
				recordFileRelatePath(subFile, resourcePath, list);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		autoList("/sql");
		
//		System.out.println(loadAll().toString());
	}
	
}
