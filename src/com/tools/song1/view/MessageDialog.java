package com.tools.song1.view;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

import com.upload.aliyun.util.StringUtil;

public class MessageDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public MessageDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public void setTextValue(String str){
		if(StringUtil.isEmptyString(str) && text != null){
			text.setText(str);
		}
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(341, 227);
		shell.setText("操作提示");
		shell.setLayout(new GridLayout(2, false));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		text = new Text(shell, SWT.MULTI | SWT.WRAP);
		text.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		text.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		text.setEditable(false);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_text.widthHint = 327;
		gd_text.heightHint = 77;
		text.setLayoutData(gd_text);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
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
