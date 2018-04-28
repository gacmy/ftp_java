
public class Step {
	private static boolean isConnect;
	private static boolean isValidateUserName;
	private static boolean isValidatePwd;
	private static boolean UPLOAD;
	private static boolean DOWNLOAD;
	
	public static void connectSuccess() {
		isConnect = true;
	}
	public static void validateUserNameSuccess() {
		isValidateUserName = true;
	}
	public static void validatePWDSuccess() {
		isValidatePwd = true;
	}
	public static void connectFailed() {
		isConnect = false;
	}
	public static void validateUserNameFailed() {
		isValidateUserName = false;
	}
	public static void validatePWDFailed() {
		isValidatePwd = false;
	}
	public static boolean isConnect() {
		return isConnect;
	}
	public static void setConnect(boolean isConnect) {
		Step.isConnect = isConnect;
	}
	public static boolean isValidateUserName() {
		return isValidateUserName;
	}
	public static void setValidateUserName(boolean isValidateUserName) {
		Step.isValidateUserName = isValidateUserName;
	}
	public static boolean isValidatePwd() {
		return isValidatePwd;
	}
	public static void setValidatePwd(boolean isValidatePwd) {
		Step.isValidatePwd = isValidatePwd;
	}
	
	//判断是否完成校验
	public static boolean isFinish() {
		return isValidatePwd && isValidateUserName && isConnect;
	}
	
}
