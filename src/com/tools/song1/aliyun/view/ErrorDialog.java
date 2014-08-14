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
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class ErrorDialog extends Dialog {
	private Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Button btnNewButton;
	private FormData fd_btnNewButton;

	public ErrorDialog(Shell parent, int style) {
		super(parent, style);
		setText("提示");
	}

	public void open(String str) {
		getParent().setEnabled(false);
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
		shell = new Shell(getParent(), SWT.CLOSE | SWT.TITLE);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				getParent().setEnabled(true);
			}
		});
		shell.setBackground(SWTResourceManager.getColor(255, 255, 255));
		shell.setText(getText());
		shell.setSize(252, 156);
		shell.setLayout(new FormLayout());
		Label lblNewLabel = formToolkit.createLabel(shell, "New Label", SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		lblNewLabel.setImage(SWTResourceManager.getImage(ErrorDialog.class, "/images/error_info.png"));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0, 20);
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		fd_lblNewLabel.right = new FormAttachment(0, 46);
		fd_lblNewLabel.bottom = new FormAttachment(0, 56);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		
		Label lblNewLabel_1 = formToolkit.createLabel(shell, str, SWT.NONE);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(255, 255, 255));
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.top = new FormAttachment(0, 30);
		fd_lblNewLabel_1.bottom = new FormAttachment(100, -45);
		fd_lblNewLabel_1.right = new FormAttachment(100,-10);
		fd_lblNewLabel_1.left = new FormAttachment(lblNewLabel, 5);
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_1);
		btnNewButton = new Button(shell, SWT.NONE);
		fd_btnNewButton = new FormData();
		fd_btnNewButton.bottom = new FormAttachment(100, -10);
		fd_btnNewButton.left = new FormAttachment(0, 81);
		fd_btnNewButton.right = new FormAttachment(100, -88);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getParent().setEnabled(true);
				shell.dispose();
			}
		});
		btnNewButton.setText("确  定");

	}
}
