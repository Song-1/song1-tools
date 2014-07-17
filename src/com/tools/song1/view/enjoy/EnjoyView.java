package com.tools.song1.view.enjoy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.tools.song1.util.JavascriptUtil;
import com.tools.song1.util.OSSUploadUtil;
import com.tools.song1.util.SystemPropertiesUtil;
import com.upload.aliyun.MusicConstants;
import org.eclipse.wb.swt.SWTResourceManager;

public class EnjoyView {

	protected Shell shell;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MusicConstants.loadConfig();
			SystemPropertiesUtil.init();
			OSSUploadUtil.init();
			JavascriptUtil.init();
			EnjoyView window = new EnjoyView();
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
		shell.setImage(SWTResourceManager.getImage(EnjoyView.class, "/images/song1.png"));
		shell.setSize(800, 600);
		shell.setText("小工具");
		shell.setLayout(new GridLayout());
		//new EnjoyComposite(shell, SWT.NONE);
		new EnjoyMainComposite(shell, SWT.NONE);
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

}
