package wang.ulane.custom.script;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Pattern;

import wang.ulane.file.ConvertUtil;
import wang.ulane.file.FileManage;

public class EclipseTerminalStr {
	
	public static void execute(String filePath, Boolean goFile) throws Exception{
//		if(filePath == null){
//			filePath = "C:\\Develop\\Install\\eclipse_mars2\\workspace\\.metadata\\.plugins\\org.eclipse.e4.workbench\\workbench.xmi";
//			goFile = false;
//		}
		if(goFile == null){
			goFile = false;
		}
		if(goFile){
			filePath = filePath + "\\.metadata\\.plugins\\org.eclipse.e4.workbench\\workbench.xmi";
		}
		String xmiStr = ConvertUtil.parseString(new FileInputStream(new File(filePath)));
		//手动匹配换行符有点困难,不搞了
//		xmiStr = xmiStr.replaceAll("(<children xsi:type=\"menu:ToolBar\".*elementId=\"org.eclipse.tm.terminal.view.ui.toolbar\">)(\r*\n*.*)*(</children>(\r*\n*.*)*<children xsi:type=\"menu:ToolControl\")", "$1$2");
		
		//反向非贪婪匹配  $2为((?!<children xsi:type=\"menu:ToolBar\").)*一个字符
		Pattern pat = Pattern.compile("(<children xsi:type=\"menu:ToolBar\"((?!<children xsi:type=\"menu:ToolBar\").)*elementId=\"org.eclipse.tm.terminal.view.ui.toolbar\">).*?((?<! )      </children>)", Pattern.DOTALL);
//		Matcher m = pat.matcher(xmiStr);
//		System.out.println(m.groupCount());
//		while(m.find()){
//			for(int i=0,length=m.groupCount()+1; i<length; i++){
//				System.out.println(m.group(i));
//			}
//		}
		
		xmiStr = pat.matcher(xmiStr).replaceAll("$1$3");
		FileManage.saveFile(filePath, xmiStr);
	}
	
}
