/**
 * 
 */
package com.songone.www.base.excecptions;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class DBUtilException extends Exception{
	private static final long serialVersionUID = -1518178668099756743L;
	private String errorMsg;
	
	public DBUtilException(){}
	
	public DBUtilException(Throwable e){
		super(e);
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
