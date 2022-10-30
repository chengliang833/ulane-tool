package wang.ulane.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileDeepCopy {
	public static void main(String[] args) throws Exception {
		System.out.println("start...");
		execute();
		System.out.println("finish...");
	}
	
	static List<String> includeSuf = Arrays.asList(".sql",".tar");
	
	public static void execute() throws Exception {
		String fromPath = "C:\\Users\\Administrator\\Desktop\\temp\\a";
		String toPath = "C:\\Users\\Administrator\\Desktop\\temp\\b";
		traverse(new File(fromPath), fromPath, toPath);
	}
	
    public static void traverse(File file, String fromPath, String toPath) throws IOException{
    	if(file.isDirectory()){
    		for(File f:file.listFiles()){
    			traverse(f, fromPath, toPath);
    		}
    	}else{
        	for(String suf:includeSuf){
        		if(file.getName().endsWith(suf)){
        			copy(file, fromPath, toPath);
        		}
        	}
    	}
    }
    
    public static void copy(File file, String fromPath, String toPath) throws IOException{
    	String toPathFile = file.getPath().replace(fromPath, toPath);
//    	System.out.println("from:"+file.getPath()+"\r\nto:"+toPathFile);
    	FileManage.saveFile(toPathFile, new FileInputStream(file));
    }
    
}
