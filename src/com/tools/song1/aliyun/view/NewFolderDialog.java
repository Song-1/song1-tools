package com.tools.song1.aliyun.view;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.util.LayoutUtil;
import com.tools.song1.util.StringUtil;

public class NewFolderDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtNewText;
	private Map<String,Object> params;
	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewFolderDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		getParent().setEnabled(false);
		createContents();
		shell.open();
		shell.layout();
		LayoutUtil.centerShell(getParent(), shell);
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
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				getParent().setEnabled(true);
			}
		});
		shell.setSize(459, 171);
		shell.setText("新建文件夹");
		
		Label lblNewLabel = formToolkit.createLabel(shell, "文件夹名称：", SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel.setBounds(10, 38, 78, 17);
		
		txtNewText = formToolkit.createText(shell, "", SWT.NONE);
		txtNewText.setBounds(88, 36, 348, 23);
		
		Button btnNewButton = formToolkit.createButton(shell, "是", SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String bucket = (String)params.get("bucket");
				String baseKey = (String)params.get("key");
				String key = txtNewText.getText();
				if(StringUtil.isEmptyString(bucket)){
					new ErrorDialog(shell, SWT.NONE).open("请选择bucket!");
				}else if(baseKey == null){
					new ErrorDialog(shell, SWT.NONE).open("请选择要新建文件夹的上层目录!");
				}else if(StringUtil.isEmptyString(key)){
					new ErrorDialog(shell, SWT.NONE).open("请输入文件夹名称!");
				}else{
					OSSUploadUtil.putObject(bucket, baseKey+key+"/", null);
				}
				getParent().setEnabled(true);
				shell.dispose();
			}
		});
		btnNewButton.setBounds(117, 86, 80, 27);
		
		Button btnNewButton_1 = formToolkit.createButton(shell, "否", SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getParent().setEnabled(true);
				shell.dispose();
			}
		});
		btnNewButton_1.setBounds(262, 86, 80, 27);

	}
}
