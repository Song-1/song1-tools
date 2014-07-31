package com.tools.song1.aliyun.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.util.LayoutUtil;
import com.tools.song1.util.StringUtil;

public class ModiyFileNameDialog extends Dialog {
	private String bucket;
	private String key;
	protected Object result;
	protected Shell shell;
	private Text text;
	private TableItem tableItem;
	private String keyName;
	private String stuffix = "";
	private boolean isModifySuccess = false;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ModiyFileNameDialog(Shell parent, int style) {
		super(parent, style);
		setText("修改文件名称");
		parent.setEnabled(false);
	}

	// public ModiyFileNameDialog(Shell parent, int style,String bucket,String
	// key,TableItem tableItem) {
	// super(parent, style);
	// setText("修改文件名称");
	// this.bucket = bucket;
	// this.key = key;
	// this.tableItem = tableItem;
	// }

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

	public TableItem getTableItem() {
		return tableItem;
	}

	public void setTableItem(TableItem tableItem) {
		if (tableItem != null) {
			key = (String) tableItem.getData();
			keyName = tableItem.getText();
		}
		this.tableItem = tableItem;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		LayoutUtil.centerShell(display, shell);
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
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				shell.getParent().setEnabled(true);
			}
		});
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				shell.getParent().setEnabled(true);
			}
		});
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
		if (!StringUtil.isEmptyString(keyName)) {
			int i = keyName.lastIndexOf(".");
			if (i > 0) {
				stuffix = new String(keyName.substring(i));
				String fileName = new String(keyName.substring(0, i));
				text.setText(fileName);
			} else {
				text.setText(keyName);

			}
		}

		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setBounds(10, 46, 65, 17);
		lblNewLabel.setText("文件名称：");

		Button btnNewButton = new Button(group, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display.getCurrent().syncExec(new ModifyFileRunnable());
				if (isModifySuccess) {
					shell.dispose();
				}
			}
		});
		btnNewButton.setBounds(481, 41, 80, 27);
		btnNewButton.setText("确  定");

	}

	private class ModifyFileRunnable implements Runnable {
		@Override
		public void run() {
			String newKey = text.getText();
			if (StringUtil.isEmptyString(bucket)) {
				new ErrorDialog(shell, SWT.NONE).open("请选择BUCKET");
			} else if (StringUtil.isEmptyString(key)) {
				new ErrorDialog(shell, SWT.NONE).open("请选择文件");
			} else if (StringUtil.isEmptyString(newKey)) {
				new ErrorDialog(shell, SWT.NONE).open("修改的文件名不能为空");
			} else if (keyName.equals(newKey + stuffix)) {
				new ErrorDialog(shell, SWT.NONE).open("修改的文件名不能和原文件名相同");
			} else {
				String keyStr = key;
				boolean flag = false;
				if (keyStr.endsWith("/")) {
					keyStr = new String(keyStr.substring(0, keyStr.length() - 1));
					flag = true;
				}
				int i = keyStr.lastIndexOf("/");
				String baseKey = "";
				if (i > 0) {
					baseKey = new String(keyStr.substring(0, i + 1));
				}
				baseKey = baseKey + newKey;
				baseKey += stuffix;
				if (flag) {
					baseKey = baseKey.trim();
					baseKey += "/";
				}
				flag = OSSUploadUtil.modifyAliyunFloderName(bucket, key, bucket, baseKey);
				// flag = true;
				if (flag) {
					OSSUploadUtil.deleteObject(bucket, key);
					tableItem.setText(newKey + stuffix);
					System.out.println(baseKey);
					tableItem.setData(baseKey);
					isModifySuccess = true;
					new ErrorDialog(shell, SWT.NONE).open("修改成功");
				} else {
					new ErrorDialog(shell, SWT.NONE).open("修改失败");
				}
			}

		}

	}
}
