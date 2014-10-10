/**
 * 
 */
package com.songone.www.base.excecptions;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SqlSessionNullPointException extends Exception {

	private static final long serialVersionUID = -281084225464522200L;
	public SqlSessionNullPointException() {
		super();
	}

	public SqlSessionNullPointException(Throwable e) {
		super(e);
	}

	public SqlSessionNullPointException(String message) {
		super(message);
	}

	public SqlSessionNullPointException(String message, Throwable cause) {
		super(message, cause);
	}

}
