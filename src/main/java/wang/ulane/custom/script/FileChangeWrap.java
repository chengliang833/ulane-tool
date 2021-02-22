package wang.ulane.custom.script;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import wang.ulane.file.ConvertUtil;
import wang.ulane.file.FileManage;

public class FileChangeWrap {
	
	private static List<String> fileTypes = Arrays.asList("txt","ntp","scss","sass","data","vue","yml","json","css","md","java","xml","js","html","properties");
	
	private String changeType;
	private String fromPath;
	private String toPath;
	public FileChangeWrap(String changeType, String fromPath, String toPath) {
		this.changeType = changeType;
		this.fromPath = fromPath;
		this.toPath = toPath;
	}

	public void execute() throws Exception{
		File file = new File(fromPath);
		if(toPath == null){
			if(file.isFile()){
				toPath = file.getParent();
			}else{
				toPath = fromPath;
			}
		}
		traverse(new File(fromPath));
	}

    public void traverse(File file) throws Exception{
    	if(file.isDirectory()){
    		for(File f:file.listFiles()){
    			traverse(f);
    		}
    	}else{
    		changeWrap(file);
    	}
    }
    
    public void changeWrap(File file) throws Exception{
    	if(!fileTypes.contains(file.getName().substring(file.getName().lastIndexOf(".")+1))){
    		return;
    	}
    	
//    	ByteArrayOutputStream baos = ConvertUtil.parseWithHexWrap("(?<!0d)0a", "0d0a", new FileInputStream(file));
    	ByteArrayOutputStream baos = null;
    	if("n".equals(changeType)){
    		baos = ConvertUtil.parseWithStrWrap("\r\n", "\n", new FileInputStream(file));
    	}else if("rn".equals(changeType)){
    		baos = ConvertUtil.parseWithStrWrap("(?<!\r)\n", "\r\n", new FileInputStream(file));
    	}
    	
    	File folder;
    	if(file.getPath().equals(fromPath)){
    		//file就是源,检查目标文件夹
    		folder = new File(toPath);
    	}else{
    		//file是源下的文件夹或文件,准备建立目标文件夹的子文件夹
    		folder = new File(toPath+file.getParent().replace(fromPath, ""));
    	}
    	//检查对应子文件夹是否存在
    	if(!folder.exists()){
    		folder.mkdirs();
    	}
    	if(baos != null){
    		FileManage.saveFile(folder.getPath()+"\\"+file.getName(), baos);
    		System.out.println("change file:"+file.getPath());
    	}else{
    		System.out.println("  skip file...");
    	}
    }
}
