/**
 * 
 */
package com.tools.song1.runnable;

/**
 * @author Administrator
 *
 */
public abstract class BaseRunnable implements Runnable {
	
	@Override
	public void run() {
		doRun();
	}
	
	public abstract void doRun();
	
	@SuppressWarnings("deprecation")
	public void stopThread(){
		try{
			Thread.currentThread().stop();
		}catch(Exception e){
			throw new RuntimeException();
		}
	}

}
