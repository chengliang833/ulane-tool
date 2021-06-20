package wang.ulane.log;

import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class LogAspect {
	protected static Logger log = LoggerFactory.getLogger(LogAspect.class);
	
	private static Integer returnLength = 1000;
	private static Integer paramsLength = 1000;
	private static Integer mybatisParamsLength = 1000;
	
	@Value("${logext.return.length:1000}")
	public void setReturnLength(Integer returnLength) {
		LogAspect.returnLength = returnLength;
	}
	@Value("${logext.params.length:1000}")
	public void setParamsLength(Integer paramsLength) {
		LogAspect.paramsLength = paramsLength;
	}

	@Value("${logext.mybatis.params.length:1000}")
	public void setMybatisParamsLength(Integer mybatisParamsLength) {
		LogAspect.mybatisParamsLength = mybatisParamsLength;
	}
	public static Integer getMybatisParamsLength() {
		return mybatisParamsLength;
	}
	
	public static Object controllerAroundInvoke(ProceedingJoinPoint joinPoint) throws Throwable{
        return aroundInvoke(joinPoint, true);
	}
    public static Object serviceAroundInvoke(ProceedingJoinPoint joinPoint) throws Throwable {
		return aroundInvoke(joinPoint, false);
    }
    public static Object aroundInvoke(ProceedingJoinPoint joinPoint, boolean maybeStream) throws Throwable {
		long start = System.currentTimeMillis();
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String target = logBefore(maybeStream, signature.getMethod(), joinPoint.getArgs());
		
		Object result = joinPoint.proceed();
		
		logAfter(maybeStream, start, result, target);
		
		return result;
    }
    
	public static Object controllerAroundInvoke(MethodInvocation invocation) throws Throwable{
        return aroundInvoke(invocation, true);
	}
    public static Object serviceAroundInvoke(MethodInvocation invocation) throws Throwable {
		return aroundInvoke(invocation, false);
    }
    public static Object aroundInvoke(MethodInvocation invocation, boolean maybeStream) throws Throwable {
		long start = System.currentTimeMillis();
		
	    String target = logBefore(maybeStream, invocation.getMethod(), invocation.getArguments());
		
		Object result = invocation.proceed();
		
		logAfter(maybeStream, start, result, target);
		
		return result;
    }
    
    
    public static String logBefore(boolean maybeStream, Method method, Object[] args){
	    Class<?> targetClass = method.getDeclaringClass();
	    String target = targetClass.getName() + ":" + method.getName();
	    if(maybeStream){
	    	JSONArray arr = new JSONArray(); 
	    	for(Object arg:args){
	    		if(arg instanceof ServletRequest || arg instanceof ServletResponse){
	    			arr.add("servlet object...");
	    		}else{
	    			arr.add(arg);
	    		}
	    	}
	    	printLogBeforeProceed(target, arr.toJSONString());
	    }else{
	    	printLogBeforeProceed(target, JSON.toJSONString(args));
	    }
    	return target;
    }
    public static void logAfter(boolean maybeStream, long start, Object result, String target){
        long timeConsuming = System.currentTimeMillis() - start;
        if(maybeStream && (result instanceof ModelAndView || (result instanceof ResponseEntity && ((ResponseEntity)result).getBody() instanceof InputStreamResource))){
        	printStreamLogAfterProceed(target, timeConsuming, result);
        }else{
        	printObjectLogAfterProceed(target, timeConsuming, result);
        }
    }
    
    
    
    
    
    
    public static void printLogBeforeProceed(String target, String params){
    	if(params.length() > paramsLength){
    		params = params.substring(0,paramsLength)+"......";
    	}
    	log.info("{}开始调用--> {} 参数:{}", Thread.currentThread().getName(), target, params);
    }
    
    public static void printStreamLogAfterProceed(String target, long timeConsuming, Object result){
    	log.info("{}结束调用<-- {} 返回值:{} 耗时:{}ms", Thread.currentThread().getName(), target, "InputStreamResource 不打印", timeConsuming);
    }
    
    public static void printObjectLogAfterProceed(String target, long timeConsuming, Object result){
        //返回值太多了，需要看再打印，如果为空是空字符串
    	String resultStr = JSONObject.toJSONString(result);
    	if(resultStr.length() > returnLength){
    		resultStr = resultStr.substring(0,returnLength)+"......";
    	}
    	log.info("{}结束调用<-- {} 返回值:{} 耗时:{}ms", Thread.currentThread().getName(), target, resultStr, timeConsuming);
    }
    
}
