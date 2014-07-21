package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.util.LayoutUtil;
import com.tools.song1.util.StringUtil;

public class MessageDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Label text;
	private Text text_1;
	private Composite composite;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public MessageDialog(Shell parent, int style) {
		super(parent, SWT.SHELL_TRIM);
		setText("SWT Dialog");
		parent.setEnabled(false);
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open(String str, String detaile) {
		createContents(str, detaile);
		shell.open();
		shell.layout();
		shell.addShellListener(new MessageShellListener());
		LayoutUtil.centerShell(shell.getParent(), shell);

		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents(String str, final String detaile) {
		shell = new Shell(getParent(), getStyle());
		shell.setLocation(500, 100);
		shell.setSize(306, 170);
		shell.setText("提示");
		shell.setLayout(new FormLayout());
		final Composite composite_1 = new Composite(shell, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(0, 63);
		fd_composite_1.right = new FormAttachment(0, 290);
		fd_composite_1.top = new FormAttachment(0);
		fd_composite_1.left = new FormAttachment(0);
		composite_1.setLayoutData(fd_composite_1);

		text = new Label(composite_1, SWT.CENTER | SWT.WRAP);
		text.setBounds(10, 25, 273, 23);
		if (!StringUtil.isEmptyString(str)) {
			text.setText(str);
		}
		composite = new Composite(shell, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(composite_1, 6);
		fd_composite.left = new FormAttachment(composite_1, 0, SWT.LEFT);
		fd_composite.bottom = new FormAttachment(0, 124);
		fd_composite.right = new FormAttachment(0, 290);
		composite.setLayoutData(fd_composite);

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.getParent().setEnabled(true);
				shell.close();
			}
		});
		btnNewButton.setText("确  定");

		if (StringUtil.isEmptyString(detaile)) {
			btnNewButton.setBounds(100, 10, 80, 27);
		} else {
			btnNewButton.setBounds(50, 10, 80, 27);
			Button btnNewButton_1 = new Button(composite, SWT.NONE);
			btnNewButton_1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					text_1 = new Text(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.LEFT);
					text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					text_1.setEditable(false);
					text_1.setBounds(10, 44, 280, 132);
					text_1.setText(detaile);
					FormData fd_composite_2 = new FormData();
					fd_composite_2.top = new FormAttachment(composite_1, 6);
					fd_composite_2.left = new FormAttachment(composite_1, 0, SWT.LEFT);
					fd_composite_2.bottom = new FormAttachment(0, 245);
					fd_composite_2.right = new FormAttachment(0, 290);
					composite.setLayoutData(fd_composite_2);
					shell.setSize(306, 300);
					composite.layout();
					shell.layout();
				}
			});
			btnNewButton_1.setBounds(160, 10, 80, 27);
			btnNewButton_1.setText("详  细");
		}
	}

	private class MessageShellListener implements ShellListener {

		@Override
		public void shellActivated(ShellEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void shellClosed(ShellEvent arg0) {
			shell.getParent().setEnabled(true);
		}

		@Override
		public void shellDeactivated(ShellEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void shellDeiconified(ShellEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void shellIconified(ShellEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

}
