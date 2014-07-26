package com.tools.song1.aliyun.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.upload.aliyun.util.StringUtil;

public class ModiyFileNameDialog extends Dialog {
	private String bucket;
	private String key;
	protected Object result;
	protected Shell shell;
	private Text text;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ModiyFileNameDialog(Shell parent, int style) {
		super(parent, style);
		setText("修改文件名称");
	}
	
	public ModiyFileNameDialog(Shell parent, int style,String bucket,String key) {
		super(parent, style);
		setText("修改文件名称");
		this.bucket = bucket;
		this.key = key;
	}
	
	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
		shell.setSize(607, 147);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		Group group = new Group(shell, SWT.NONE);
		group.setText("  修改文件名称：：");
		group.setLayout(null);
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(0, 99);
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		fd_group.right = new FormAttachment(0, 581);
		group.setLayoutData(fd_group);
		
		text = new Text(group, SWT.BORDER);
		text.setBounds(77, 43, 400, 23);
		text.setText(key);
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setBounds(10, 46, 65, 17);
		lblNewLabel.setText("文件名称：");
		
		Button btnNewButton = new Button(group, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display.getCurrent().syncExec(new ModifyFileRunnable());
			}
		});
		btnNewButton.setBounds(481, 41, 80, 27);
		btnNewButton.setText("确  定");

	}
	
	private class ModifyFileRunnable implements Runnable{
		@Override
		public void run() {
			String newKey = text.getText();
			if(StringUtil.isEmptyString(bucket)){
				new ErrorDialog(shell,SWT.NONE).open("请选择BUCKET");
			}else if(StringUtil.isEmptyString(key)){
				new ErrorDialog(shell,SWT.NONE).open("请选择文件");
			}else if(StringUtil.isEmptyString(newKey)){
				new ErrorDialog(shell,SWT.NONE).open("修改的文件名不能为空");
			}else if(key.equals(newKey)){
				new ErrorDialog(shell,SWT.NONE).open("修改的文件名不能和原文件名相同");
			}else{
				new ErrorDialog(shell,SWT.NONE).open("OK");
			}
			
		}
		
	}
	
	private class ErrorDialog extends Dialog{
		private Shell shell;
		public ErrorDialog(Shell parent, int style) {
			super(parent, style);
			setText("提示");
		}
		
		public Object open(String str) {
			createContents(str);
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
		
		private void createContents(String str) {
			shell = new Shell(getParent(), SWT.SHELL_TRIM);
			shell.setText(getText());
			shell.setSize(220, 120);
			shell.setLayout(null);
			Label lblNewLabel = new Label(shell, SWT.NONE);
			lblNewLabel.setBounds(10, 25, 180, 25);
			lblNewLabel.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/log_error2.png"));
			lblNewLabel.setText(str);
			
			Button btnNewButton = new Button(shell, SWT.NONE);
			btnNewButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					shell.dispose();
				}
			});
			btnNewButton.setBounds(70, 55, 80, 27);
			btnNewButton.setText("确  定");

		}
		
	}
}
