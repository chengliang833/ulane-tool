package wang.ulane.proxy;

public enum MethodParamTypeEnum {
	/**
	 * 全方法名
	 */
	AROUND_FULL_NAME,
	/**
	 * around下，before after方法体
	 */
	AROUND_TWO_BODY_STR,
	/**
	 * before after全方法名
	 */
	BEFOR_AFTER_FULL_NAME;
	
	public static boolean isAround(MethodParamTypeEnum methodParamTypeEnum){
		return methodParamTypeEnum == AROUND_FULL_NAME || methodParamTypeEnum == AROUND_TWO_BODY_STR;
	}
	
}
