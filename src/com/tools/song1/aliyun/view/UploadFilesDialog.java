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
import org.eclipse.swt.widgets.Group;

public class UploadFilesDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;

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
		shell.setSize(722, 524);
		shell.setText("文件上传");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(0, 10, 706, 476);
		
		Composite composite = new Composite(composite_1, SWT.NONE);
		composite.setLocation(5, 260);
		composite.setSize(701, 34);
		
		ProgressBar progressBar = new ProgressBar(composite, SWT.NONE);
		progressBar.setLocation(43, 10);
		progressBar.setSize(648, 17);
		progressBar.setSelection(34);
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(0, 10, 50, 17);
		lblNewLabel.setText("进度：");
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setBounds(0, 295, 706, 172);
		
		text = new Text(composite_2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text.setBounds(5, 0, 696, 172);
		
		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setBounds(0, 0, 706, 254);
		
		Button btnNewButton_1 = new Button(composite_3, SWT.NONE);
		btnNewButton_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 11, SWT.NORMAL));
		btnNewButton_1.setBounds(253, 207, 188, 37);
		btnNewButton_1.setText("开  始  上  传");
		
		Label label = new Label(composite_3, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 33, 706, 2);
		
		Label lblNewLabel_2 = new Label(composite_3, SWT.NONE);
		lblNewLabel_2.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel_2.setBounds(10, 0, 277, 33);
		lblNewLabel_2.setText("上传文件到阿里云：");
		
		Group group = new Group(composite_3, SWT.NONE);
		group.setBounds(10, 139, 691, 62);
		
		text_1 = new Text(group, SWT.BORDER | SWT.READ_ONLY);
		text_1.setLocation(77, 20);
		text_1.setSize(490, 25);
		
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setBounds(10, 23, 65, 20);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_1.setText("选择文件：");
		
		Button btnNewButton = new Button(group, SWT.NONE);
		btnNewButton.setBounds(574, 19, 107, 27);
		btnNewButton.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		btnNewButton.setText("选   择");
		
		Group group_1 = new Group(composite_3, SWT.NONE);
		group_1.setBounds(10, 39, 686, 94);
		
		Label lblNewLabel_3 = new Label(group_1, SWT.NONE);
		lblNewLabel_3.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_3.setBounds(10, 22, 61, 17);
		lblNewLabel_3.setText("BUCKET:");
		
		text_2 = new Text(group_1, SWT.BORDER | SWT.READ_ONLY);
		text_2.setBounds(77, 20, 490, 23);
		
		Label lblNewLabel_4 = new Label(group_1, SWT.NONE);
		lblNewLabel_4.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_4.setBounds(10, 55, 61, 17);
		lblNewLabel_4.setText("上传路径：");
		
		text_3 = new Text(group_1, SWT.BORDER | SWT.READ_ONLY);
		text_3.setBounds(77, 52, 490, 23);

	}
}
