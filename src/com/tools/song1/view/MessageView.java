package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.util.StringUtil;

public class MessageView {

	protected Shell shell;
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MessageView window = new MessageView();
			window.open("sss");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open(String str) {
		if(StringUtil.isEmptyString(str)){
			return;
		}
		Display display = Display.getDefault();
		createContents(str);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(String str) {
		shell = new Shell();
		shell.setText("SWT Application");
		shell.setSize(341, 210);
		shell.setText("操作提示");
		shell.setLayout(new GridLayout(1, false));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		text = new Text(shell, SWT.WRAP | SWT.CENTER | SWT.MULTI);
		text.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		text.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		text.setEditable(false);
		text.setText(str);
//		gd_text.widthHint = 327;
//		gd_text.heightHint = 77;
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.heightHint = 32;
		gd_btnNewButton.widthHint = 93;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("确定");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}

}
