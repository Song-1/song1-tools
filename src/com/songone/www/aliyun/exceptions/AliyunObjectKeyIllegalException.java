/**
 * 
 */
package com.songone.www.aliyun.exceptions;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class AliyunObjectKeyIllegalException extends Exception{
	private static final long serialVersionUID = 3888060333982019714L;

	public AliyunObjectKeyIllegalException() {
		super();
	}

	public AliyunObjectKeyIllegalException(Throwable e) {
		super(e);
	}

	public AliyunObjectKeyIllegalException(String message) {
		super(message);
	}

	public AliyunObjectKeyIllegalException(String message, Throwable cause) {
		super(message, cause);
	}
}
