package wang.ulane.xml;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class XmlAnalyse {

    private static Logger log = LoggerFactory.getLogger(XmlAnalyse.class);  
    
	public static void main(String[] args) throws Exception {
		Document doc = null;
		SAXReader sax = new SAXReader();
		doc = sax.read(new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\temp.txt")));
		JSONArray arr = new JSONArray();
		JSONArray deepArr = new JSONArray();
		analyse(doc.nodeIterator(), 1, 4, arr, 3, deepArr);
		System.out.println(arr.toJSONString());
		System.out.println(deepArr.toJSONString());
		System.out.println("finish");
	}
	
	/**
	 * 
	 * @param nodeIt node链
	 * @param curNum 当前递归层次
	 * @param allNum 总共需要解析的层级
	 * @param arr 解析到对应的jsonarr对象
	 * @param deepNum 所有最深层次节点指定到哪一层级去收集集合
	 * @param deepArr 所有最深层次节点指定层级的平级对象
	 */
	public static void analyse(Iterator<Node> nodeIt, int curNum, int allNum, JSONArray arr, int deppNum, JSONArray deepArr){
		while(nodeIt.hasNext()){
			Node node = nodeIt.next();
			if(!(node instanceof Element)){
				continue;
			}
			Element ele = (Element)node;
			JSONObject obj = new JSONObject();
			arr.add(obj);
			JSONArray subArr = new JSONArray();
			obj.put("name", ele.getName());
			obj.put("subNodes", subArr);
			if(curNum == allNum){
				obj.put("contHtml", ele.asXML());
				obj.put("contText", ele.getStringValue());
			}
			if(curNum == deppNum){
				deepArr.add(obj);
			}
			if(curNum < allNum){
				analyse(ele.nodeIterator(), curNum+1, allNum, subArr, deppNum, deepArr);
			}
			if(curNum > allNum){
				throw new RuntimeException("error...");
			}
		}
	}
	
}
