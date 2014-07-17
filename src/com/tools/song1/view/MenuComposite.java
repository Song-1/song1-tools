package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.util.StringUtil;
import com.tools.song1.view.enjoy.EnjoyComposite;
import com.upload.aliyun.MusicConstants;

public class MenuComposite extends Composite {
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MenuComposite(final Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, true));
		
				Group group = new Group(this, SWT.NONE);
				group.setText("全局设置");
				group.setLayout(new GridLayout(2, false));
				GridData gd_group = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
				gd_group.heightHint = 107;
				gd_group.widthHint = 783;
				group.setLayoutData(gd_group);
				
						Label lblNewLabel = new Label(group, SWT.NONE);
						lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
						lblNewLabel.setText("阿里云buket：");
						
								text = new Text(group, SWT.BORDER);
								text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
								
										Label lblNewLabel_1 = new Label(group, SWT.NONE);
										lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
										lblNewLabel_1.setText("阿里云根目录：");
										
												text_1 = new Text(group, SWT.BORDER);
												text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
												new Label(group, SWT.NONE);
												
														Button btnNewButton_3 = new Button(group, SWT.NONE);
														GridData gd_btnNewButton_3 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
														gd_btnNewButton_3.widthHint = 86;
														btnNewButton_3.setLayoutData(gd_btnNewButton_3);
														btnNewButton_3.setText("保存");
														btnNewButton_3.addSelectionListener(new SelectionAdapter() {
															public void widgetSelected(SelectionEvent e) {
																if (!StringUtil.isEmptyString(text.getText())) {
																	MusicConstants.BUKET_NAME = text.getText();
																}
																if (!StringUtil.isEmptyString(text_1.getText())) {
																	MusicConstants.SERVER_PATH_ROOT = text_1.getText();
																}
																MessageView d = new MessageView();
																d.open("保存成功");
															}
														});
		setSize(800, 600);
		
		Group group_1 = new Group(this, SWT.NONE);
		group_1.setText("功能菜单");
		group_1.setLayout(new GridLayout(1, false));
		GridData gd_group_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_1.heightHint = 165;
		gd_group_1.widthHint = 783;
		group_1.setLayoutData(gd_group_1);
		
//		text_3 = new Text(group_1, SWT.BORDER);
//		text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton = new Button(group_1, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_btnNewButton.heightHint = 48;
		gd_btnNewButton.widthHint = 149;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("樱桃时光");
		btnNewButton.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		new Label(this, SWT.NONE);
		Button btnNewButton_1 = new Button(group_1, SWT.NONE);
		GridData gd_btnNewButton_1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_1.heightHint = 48;
		gd_btnNewButton_1.widthHint = 149;
		btnNewButton_1.setLayoutData(gd_btnNewButton_1);
		btnNewButton_1.setText("状元听书");
		btnNewButton_1.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		new Label(this, SWT.NONE);

		final Button btnNewButton_2 = new Button(group_1, SWT.NONE);
		btnNewButton_2.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		GridData gd_btnNewButton_2 = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_btnNewButton_2.heightHint = 48;
		gd_btnNewButton_2.widthHint = 149;
		btnNewButton_2.setLayoutData(gd_btnNewButton_2);
		btnNewButton_2.setText("享CD");
		
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		text_2 = new Text(this, SWT.NONE);
		text_2.setEditable(false);
		text_2.setEnabled(false);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Shell shell = btnNewButton_2.getShell();
				btnNewButton_2.getParent().dispose();
				new EnjoyComposite(shell, SWT.NONE);
				shell.open();
				shell.layout();
				shell.pack();
				
			}
		});

		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
