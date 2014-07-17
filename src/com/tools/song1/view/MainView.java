package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.tools.song1.util.JavascriptUtil;
import com.tools.song1.util.OSSUploadUtil;
import com.upload.aliyun.MusicConstants;

public class MainView {

	protected Shell shell;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			///// init configs
			MusicConstants.loadConfig();
			OSSUploadUtil.init();
			JavascriptUtil.init();
			///// open window
			MainView window = new MainView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		shell = new Shell(display);
		shell.setSize(800, 600);
		shell.setText("小工具");
		shell.setLayout(new GridLayout());
		new MenuComposite(shell, SWT.NONE);
		shell.open();
		shell.layout();
		shell.pack();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
	public void displayShell(Shell shellView){
		shellView.open();
		shellView.layout();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 600);
		shell.setText("小工具");
		shell.setLayout(new GridLayout());
		new MenuComposite(shell, SWT.NONE);
	}
}
