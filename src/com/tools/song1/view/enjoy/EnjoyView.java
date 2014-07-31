package com.tools.song1.view.enjoy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.util.JavascriptUtil;
import com.tools.song1.util.MyPrintStream;
import com.tools.song1.util.SystemPropertiesUtil;
import com.upload.aliyun.MusicConstants;

public class EnjoyView {

	protected Shell shell;
	private Text text;

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
		shell.setSize(1200, 600);
		shell.setText("小工具");
		shell.setLayout(new GridLayout());
		// new EnjoyComposite(shell, SWT.NONE);
		EnjoyByAlbumStyleComposite enjoy = new EnjoyByAlbumStyleComposite(shell, SWT.NONE);
		text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setBounds(810, 10, 150, 500);
		// ///输出重定向设置
		MyPrintStream mps = new MyPrintStream(System.out, text);
		System.setOut(mps);
		System.setErr(mps);
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
