package com.tools.song1.aliyun.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.util.LayoutUtil;

public class ErrorDialog extends Dialog {
	private Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	public ErrorDialog(Shell parent, int style) {
		super(parent, style);
		setText("提示");
	}

	public void open(String str) {
		createContents(str);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		LayoutUtil.centerShell(display, shell);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void createContents(String str) {
		shell = new Shell(getParent(), SWT.SHELL_TRIM);
		shell.setText(getText());
		shell.setSize(329, 188);
		shell.setLayout(null);

		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnNewButton.setBounds(111, 81, 80, 27);
		btnNewButton.setText("确  定");

		Label lblNewLabel = formToolkit.createLabel(shell, "New Label", SWT.NONE);
		lblNewLabel.setImage(SWTResourceManager.getImage(ErrorDialog.class, "/images/log_error2.png"));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel.setBounds(38, 38, 21, 17);

		Label lblNewLabel_1 = formToolkit.createLabel(shell, str, SWT.NONE);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 11, SWT.NORMAL));
		lblNewLabel_1.setBounds(65, 34, 222, 22);

	}

}
