/**
 * 
 */
package com.songone.www.base.excecptions;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class ValidateExecuteMethodException extends Exception{
	private static final long serialVersionUID = 3068514691238489625L;

	public ValidateExecuteMethodException() {
		super();
	}

	public ValidateExecuteMethodException(Throwable e) {
		super(e);
	}

	public ValidateExecuteMethodException(String message) {
		super(message);
	}

	public ValidateExecuteMethodException(String message, Throwable cause) {
		super(message, cause);
	}
}
