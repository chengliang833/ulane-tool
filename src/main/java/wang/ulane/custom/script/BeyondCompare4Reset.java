package wang.ulane.custom.script;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import wang.ulane.file.ConvertUtil;
import wang.ulane.file.FileManage;

public class BeyondCompare4Reset {
	
	private static List<String> noDeletes = Arrays.asList("Helpers");
	
	public static void execute(String folderPath) throws Exception{
//		if(folderPath == null){
//			folderPath = "C:\\Users\\eshonulane\\AppData\\Roaming\\Scooter Software\\Beyond Compare 4";
//		}
		File folder = new File(folderPath);
		for(File file:folder.listFiles()){
			if("BCSessions.xml".equals(file.getName())){
				String xmiStr = ConvertUtil.parseString(new FileInputStream(file));
				xmiStr = xmiStr.replaceAll("<BCSessions Flags=\"\\d*\"", "<BCSessions");
				FileManage.saveFile(file.getPath(), xmiStr);
			}else if(!noDeletes.contains(file.getName())){
				file.delete();
			}
		}
		
	}
	
}
