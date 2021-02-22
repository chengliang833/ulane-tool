package wang.ulane.custom.script;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import wang.ulane.file.ConvertUtil;

public class EclipseTerminalDocument {
	
	public static void execute(String filePath) throws Exception{
		if(filePath == null){
			filePath = "C:\\Develop\\Install\\eclipse_mars2\\workspace\\.metadata\\.plugins\\org.eclipse.e4.workbench\\workbench.xmi";
		}
		//或saxreader
		String xmiStr = ConvertUtil.parseString(new FileInputStream(new File(filePath)));
		Document doc = DocumentHelper.parseText(xmiStr);
//		Element rootEle = doc.getRootElement();
//		Iterator<Attribute> attrsIt = rootEle.attributeIterator();
//		while(attrsIt.hasNext()){
//			Attribute attr = attrsIt.next();
//			System.out.println(attr.getName()+":"+attr.getValue());
//		}
//		System.out.println("-------------------------------------------------------");
//		Iterator<Element> it = rootEle.elementIterator();
//		while(it.hasNext()){
//			Element ele = it.next();
//			System.out.println(ele.getName()+":"+ele.getText());
//		}
		
		//application -> children -> trimBars:elementId="org.eclipse.ui.trim.status" -> children:elementId="org.eclipse.tm.terminal.view.ui.toolbar"
//		List<Node> nodes = doc.selectNodes("//application:Application/children/trimBars");
//		for(Node node:nodes){
//			System.out.println(node.getName()+":"+node.getText().replaceAll("\\s*", ""));
//			Element ele = (Element)node;
//			System.out.println(ele.attribute("elementId").getValue());
//		}
		Element ele = getElementByAttrEleId(doc.selectNodes("//application:Application/children/trimBars"), "org.eclipse.ui.trim.status");
		Element ele2 = getElementByAttrEleId(ele.selectNodes("trimBars/children"), "org.eclipse.tm.terminal.view.ui.toolbar");
//		System.out.println(ele2.getName()+":"+ele2.asXML());
		Iterator<Element> it = ele2.elementIterator();
		while(it.hasNext()){
			Element element = it.next();
//			System.out.println(element.getName());
			it.remove();
		}
		ele2.setText("");
//		System.out.println(ele2.asXML());
//		System.out.println(doc.toString());
//		System.out.println(doc.asXML());
		
		//重置文件
//		OutputFormat fomat = OutputFormat.createPrettyPrint();
////		fomat.setEncoding("gb2312");
//		XMLWriter writer=new XMLWriter(new FileWriter("C:\\Users\\eshonulane\\Desktop\\temp\\test2\\workbench2.xmi"),fomat);
//		writer.write(doc);
//		writer.close();
		
//		FileManage.saveFile("C:\\Users\\eshonulane\\Desktop\\temp\\test2\\workbench2.xmi", doc);
		
//		Element ele = doc.elementByID("org.eclipse.tm.terminal.view.ui.toolbar");
//		System.out.println(ele.getName()+":"+ele.getText());
	}
	
	public static Element getElementByAttrEleId(List<Node> nodes, String elementId){
		for(Node node:nodes){
			Element ele = (Element)node;
			if(elementId.equals(ele.attribute("elementId").getValue())){
				return ele;
			}
		}
		return null;
	}
	
}
