package wang.ulane.custom.script;

public class CustomAll {
	public static void main(String[] args) throws Exception {
//		args = new String[]{"bc4_reset", "C:\\Users\\eshonulane\\Desktop\\temp\\Beyond Compare 4"};
//		args = new String[]{"file_changewrap_n", "C:\\Users\\eshonulane\\Desktop\\temp\\test"};
//		args = new String[]{"eclipse_terminal", "C:\\Users\\eshonulane\\Desktop\\temp\\test2\\workbench.xmi"};
		
		System.out.println("start...");
		if(args == null || args.length <= 0){
			throw new RuntimeException("要入参的");
		}
		
		String path = args.length>1?args[1]:null;
		String path2 = args.length>2?args[2]:null;
		
		switch(args[0]){
			case "eclipse_terminal":
				EclipseTerminalStr.execute(path, false);
				break;
			case "eclipse_terminal_workspace":
				EclipseTerminalStr.execute(path, true);
				break;
			case "eclipse_svnname":
				EclipseSvnName.execute(path);
				break;
			case "bc4_reset":
				BeyondCompare4Reset.execute(path);
				break;
			case "file_changewrap_n":
				new FileChangeWrap("n", path, path2).execute();
				break;
			case "file_changewrap_rn":
				new FileChangeWrap("rn", path, path2).execute();
				break;
			default:
				System.out.println("no selected...");
		}
		System.out.println("finish...");
	}
}
