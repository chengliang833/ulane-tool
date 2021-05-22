package wang.ulane.log;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogImpl implements Log {

  private Logger log;

  public CustomLogImpl(String clazz) {
	  log = LoggerFactory.getLogger(clazz);
  }

  public boolean isDebugEnabled() {
    return true;
  }

  public boolean isTraceEnabled() {
    return false;
  }

  public void error(String s, Throwable e) {
    log.error(s, e);
  }

  public void error(String s) {
    log.error(s);
  }

  public void debug(String s) {
//	  System.out.println("debug here："+s);
	//改为info打印（陈年老代码debug配置不了哇）
  	if(s.length() > LogAspect.getMybatisParamsLength()){
		s = s.substring(0, LogAspect.getMybatisParamsLength())+"......";
	}
    log.info("{}:{}", log.getName(), s);
  }

  public void trace(String s) {
	//sql结果集，设置trace就打印
    log.trace(s);
  }

  public void warn(String s) {
    log.warn(s);
  }
}
