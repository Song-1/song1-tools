package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.upload.aliyun.util.FileDoUtil;
import com.upload.aliyun.util.StringUtil;

public class TestWinView {

	protected Shell shell;
	private Text txtAs;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_6;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TestWinView window = new TestWinView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
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
	protected void createContents() {
		shell = new Shell();
		shell.setSize(810, 600);
		shell.setText("音乐一号");
		shell.setLayout(new GridLayout(1, false));

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_tabFolder.heightHint = 350;
		gd_tabFolder.widthHint = 785;
		tabFolder.setLayoutData(gd_tabFolder);
		TabItem tabItem_2 = new TabItem(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL );
		tabItem_2.setText("   享CD   ");

		Group group_2 = new Group(tabFolder, SWT.NONE);
		tabItem_2.setControl(group_2);
		group_2.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel_2 = new Label(group_2, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		lblNewLabel_2.setText("====================== 设置 ========================");
		
		Label lblNewLabel_3 = new Label(group_2, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("阿里云buket：");
		
		text_2 = new Text(group_2, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group_2, SWT.NONE);
		
		Label lblNewLabel_4 = new Label(group_2, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("阿里云根目录：");
		
		text_3 = new Text(group_2, SWT.BORDER);
		text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group_2, SWT.NONE);
		
		Label lblNewLabel_5 = new Label(group_2, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setText("服务器API：");
		
		text_4 = new Text(group_2, SWT.BORDER);
		text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group_2, SWT.NONE);
		
		Label lblNewLabel_6 = new Label(group_2, SWT.NONE);
		lblNewLabel_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_6.setText("地域根目录：");
		
		text_5 = new Text(group_2, SWT.BORDER);
		text_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton = new Button(group_2, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.heightHint = 32;
		gd_btnNewButton.widthHint = 75;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("选择");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String singerTypeFloder = FileDoUtil.folderDig(shell);
				if (!StringUtil.isEmptyString(singerTypeFloder)) {
					text_5.setText(singerTypeFloder);
				}
			}
		});
		
		Label lblNewLabel_7 = new Label(group_2, SWT.NONE);
		lblNewLabel_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_7.setText("风格根目录：");
		
		text_6 = new Text(group_2, SWT.BORDER);
		text_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton_1 = new Button(group_2, SWT.NONE);
		GridData gd_btnNewButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_1.widthHint = 75;
		gd_btnNewButton_1.heightHint = 32;
		btnNewButton_1.setLayoutData(gd_btnNewButton_1);
		btnNewButton_1.setText("选择");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String str = FileDoUtil.folderDig(shell);
				if (!StringUtil.isEmptyString(str)) {
					text_6.setText(str);
				}
			}
		});
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		
		Button btnNewButton_2 = new Button(group_2, SWT.NONE);
		GridData gd_btnNewButton_2 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_2.heightHint = 45;
		gd_btnNewButton_2.widthHint = 161;
		btnNewButton_2.setLayoutData(gd_btnNewButton_2);
		btnNewButton_2.setText("开始处理");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String buket = text_2.getText();
				String aliyun_server_path  = text_3.getText();
				String serverUrl = text_4.getText();
				String singerType = text_5.getText();
				String albumStyle = text_6.getText();
			}
		});
		new Label(group_2, SWT.NONE);

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("  殷桃时光      ");
		Group group_1 = new Group(tabFolder, SWT.NONE);
		tabItem.setControl(group_1);
		group_1.setLayout(new GridLayout(2, false));

		Label lblNewLabel = new Label(group_1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("buket：");

		text = new Text(group_1, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_1 = new Label(group_1, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("根目录：");

		text_1 = new Text(group_1, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("   状元听书     ");

		Group group = new Group(shell, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group.heightHint = 180;
		gd_group.widthHint = 785;
		group.setLayoutData(gd_group);
		group.setText("日志输出");

		txtAs = new Text(group, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		txtAs.setEditable(true);
		txtAs.setBounds(2, 21, 784, 150);

	}
}
