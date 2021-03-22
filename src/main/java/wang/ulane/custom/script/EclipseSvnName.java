package wang.ulane.custom.script;

import java.io.File;

public class EclipseSvnName {
	
	public static void execute(String folderPath){
//		if(folderPath == null){
//			folderPath = "C:\\Develop\\Install\\eclipse_mars2\\dropins";
//		}
		File folder = new File(folderPath);
		for(File file:folder.listFiles()){
//			System.out.println(file.getName());
			if(file.isDirectory() && file.getName().startsWith("site-")){
				String changeVersion = file.getName().substring(file.getName().lastIndexOf("_")+1);
				int cv = Integer.parseInt(changeVersion);
				cv++;
				String newName = file.getName().substring(0, file.getName().lastIndexOf("_")+1) + cv;
//				System.out.println(newName);
				file.renameTo(new File(file.getParent()+"/"+newName));
			}
		}
	}
	
}
