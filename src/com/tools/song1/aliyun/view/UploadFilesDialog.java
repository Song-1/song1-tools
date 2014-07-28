package com.tools.song1.aliyun.view;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;

public class UploadFilesDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public UploadFilesDialog(Shell parent, int style) {
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

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.SHELL_TRIM);
		shell.setSize(722, 401);
		shell.setText("文件上传");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(10, 10, 686, 343);
		
		Composite composite = new Composite(composite_1, SWT.NONE);
		composite.setLocation(0, 130);
		composite.setSize(686, 34);
		
		ProgressBar progressBar = new ProgressBar(composite, SWT.NONE);
		progressBar.setLocation(43, 10);
		progressBar.setSize(633, 17);
		progressBar.setSelection(34);
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(0, 10, 50, 17);
		lblNewLabel.setText("进度：");
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setBounds(0, 167, 686, 172);
		
		text = new Text(composite_2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text.setBounds(0, 0, 686, 172);
		
		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setBounds(0, 0, 686, 124);
		
		text_1 = new Text(composite_3, SWT.BORDER);
		text_1.setBounds(81, 49, 472, 25);
		
		Label lblNewLabel_1 = new Label(composite_3, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_1.setBounds(10, 52, 65, 20);
		lblNewLabel_1.setText("选择文件：");
		
		Button btnNewButton = new Button(composite_3, SWT.NONE);
		btnNewButton.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		btnNewButton.setBounds(559, 48, 107, 27);
		btnNewButton.setText("选   择");
		
		Button btnNewButton_1 = new Button(composite_3, SWT.NONE);
		btnNewButton_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 11, SWT.NORMAL));
		btnNewButton_1.setBounds(258, 87, 188, 37);
		btnNewButton_1.setText("开  始  上  传");
		
		Label label = new Label(composite_3, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 33, 686, 2);
		
		Label lblNewLabel_2 = new Label(composite_3, SWT.NONE);
		lblNewLabel_2.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel_2.setBounds(10, 0, 277, 33);
		lblNewLabel_2.setText("上传文件到阿里云：");

	}
}
