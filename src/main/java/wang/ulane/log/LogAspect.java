package wang.ulane.log;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

public class LogAspect {
	protected static Logger log = LoggerFactory.getLogger(LogAspect.class);
	
	private static Integer returnLength = 1000;
	private static Integer paramsLength = 1000;
	
	@Value("${logext.return.length:1000}")
	public void setReturnLength(Integer returnLength) {
		LogAspect.returnLength = returnLength;
	}
	@Value("${logext.params.length:1000}")
	public void setParamsLength(Integer paramsLength) {
		LogAspect.paramsLength = paramsLength;
	}

	public Object controllerAroundInvoke(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String target = logBefore(signature.getMethod(), joinPoint.getArgs());
        
//        HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//        log.info("sessionid:"+request.getSession().getId());
        
		Object result = joinPoint.proceed();
		
		logAfter(start, result, target);
		
        return result;
	}
	public Object controllerAroundInvoke(MethodInvocation invocation) throws Throwable{
        long start = System.currentTimeMillis();
        
        String target = logBefore(invocation.getMethod(), invocation.getArguments());
        
		Object result = invocation.proceed();
		
		logAfter(start, result, target);
		
        return result;
	}
	
    public Object serviceAroundInvoke(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String target = logBefore(signature.getMethod(), joinPoint.getArgs());
		
		Object result = joinPoint.proceed();
		
		printObjectLogAfterProceed(target, System.currentTimeMillis() - start, result);
		
		return result;
    }
    public Object serviceAroundInvoke(MethodInvocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		
	    String target = logBefore(invocation.getMethod(), invocation.getArguments());
		
		Object result = invocation.proceed();
		
		printObjectLogAfterProceed(target, System.currentTimeMillis() - start, result);
		
		return result;
    }
    public String logBefore(Method method, Object[] args){
	    Class<?> targetClass = method.getDeclaringClass();
	    String target = targetClass.getName() + ":" + method.getName();
		printLogBeforeProceed(target, JSONObject.toJSONString(args));
    	return target;
    }
    public void logAfter(long start, Object result, String target){
        long timeConsuming = System.currentTimeMillis() - start;
        if(result instanceof ModelAndView || (result instanceof ResponseEntity && ((ResponseEntity)result).getBody() instanceof InputStreamResource)){
        	printStreamLogAfterProceed(target, timeConsuming, result);
        }else{
        	printObjectLogAfterProceed(target, timeConsuming, result);
        }
    }
    
    
    
    
    
    
    public static void printLogBeforeProceed(String target, String params){
    	if(params.length() > paramsLength){
    		params = params.substring(0,paramsLength)+"......";
    	}
    	log.info("开始{}调用--> {} 参数:{}", Thread.currentThread().getName(), target, params);
    }
    
    public static void printStreamLogAfterProceed(String target, long timeConsuming, Object result){
    	log.info("结束{}调用<-- {} 返回值:{} 耗时:{}ms", Thread.currentThread().getName(), target, "InputStreamResource 不打印", timeConsuming);
    }
    
    public static void printObjectLogAfterProceed(String target, long timeConsuming, Object result){
        //返回值太多了，需要看再打印，如果为空是空字符串
    	String resultStr = JSONObject.toJSONString(result);
    	if(resultStr.length() > returnLength){
    		resultStr = resultStr.substring(0,returnLength)+"......";
    	}
//    	log.info("结束{}调用<-- {} 返回值:{} 耗时:{}ms", Thread.currentThread().getName(), target, JSONObject.toJSONString(result), timeConsuming);
    	log.info("结束{}调用<-- {} 返回值:{} 耗时:{}ms", Thread.currentThread().getName(), target, resultStr, timeConsuming);
    }
    
}
