package wang.ulane.scriptengine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JS {
	public static void main(String[] args) throws ScriptException {
		ScriptEngine se = new ScriptEngineManager().getEngineByName("JavaScript");
		se.put("b", 32);
		se.put("a", 232);
		se.eval("c = 0.3 - 0.2;");
		System.out.println(se.get("c"));
	}
}
