package wang.ulane.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wang.ulane.custom.script.FileChangeWrap;

public class FraverseFile {
	
    private static String fromPath = "D:\\Develop\\work\\developfile\\vscode\\vue-cli\\my-demo\\src";
    private static String toPath = null;
    private static List<String> paths = new ArrayList<String>();
    static {
    	paths = Arrays.asList(
//    			"D:\\Develop\\work\\developfile\\vscode\\vue-cli\\my-demo\\babel.config.js",
//    			"D:\\Develop\\work\\developfile\\vscode\\vue-cli\\my-demo\\package.json",
//    			"D:\\Develop\\work\\developfile\\vscode\\vue-cli\\my-demo\\package-lock.json",
//    			"D:\\Develop\\work\\developfile\\vscode\\vue-cli\\my-demo\\vue.config.js",
//    			"D:\\Develop\\work\\developfile\\vscode\\vue-cli\\my-demo\\README.md"
				);
    }
    
    public static void main(String[] args) throws Exception {
    	System.out.println("start...");
    	if(paths.size() > 0){
    		changeWrapList(paths);
    	}else{
    		new FileChangeWrap("n", fromPath, toPath).execute();
    	}
    	System.out.println("finish...");
	}
    
    public static void changeWrapList(List<String> paths) throws Exception{
    	for(String path:paths){
    		File file = new File(path);
    		new FileChangeWrap("n", file.getPath(), file.getParent()).changeWrap(file);
    	}
    }
    
}