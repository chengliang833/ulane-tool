package wang.ulane.proxy;

import javassist.CtClass;
import javassist.CtConstructor;

public class ProxyClassBeforeAfter {
	
	protected static void createConstructorBefore(CtConstructor cm, MethodParam mp, CtClass[] ctParams, CtClass ct) throws Exception{
		if(mp.getBeforeContent() == null){
			return;
		}
		StringBuilder funcStr = new StringBuilder();
		funcStr.append(mp.getBeforeContent()).append("();");
		cm.insertBefore(funcStr.toString());
	}

	protected static void createConstructorAfter(CtConstructor cm, MethodParam mp, CtClass[] ctParams, CtClass ct) throws Exception{
		if(mp.getAfterContent() == null){
			return;
		}
		StringBuilder funcStr = new StringBuilder();
		funcStr.append(mp.getAfterContent()).append("($0);");
//		funcStr.append("System.out.println($0);");
		cm.insertAfter(funcStr.toString());
	}
	
}
