package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.constants.EnjoyConstans;
import com.tools.song1.constants.GlobalConstans;
import com.tools.song1.util.StringUtil;
import com.tools.song1.util.SystemPropertiesUtil;

public class GlobalSettingComposite extends Composite {
	private Text text;
	private Text text_1;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GlobalSettingComposite(Composite parent, int style) {
		super(parent, style);
		setSize(780,300);
		setLayout(new FormLayout());
		
		Group group = new Group(this, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		fd_group.bottom = new FormAttachment(0, 102);
		fd_group.right = new FormAttachment(0, 770);
		group.setLayoutData(fd_group);
		group.setText("全局设置");
		group.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("阿里云BUCKET：");
		
		text = new Text(group, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(group, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("阿里云根目录：");
		
		text_1 = new Text(group, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String globalAliyunBucket = text.getText();
				String globalAliyunPathRoot = text_1.getText();
				if(!StringUtil.isEmptyString(globalAliyunBucket)){
					SystemPropertiesUtil.addProperty(GlobalConstans.ALIYUN_BUCKET, globalAliyunBucket);
				}
				if(!StringUtil.isEmptyString(globalAliyunPathRoot)){
					SystemPropertiesUtil.addProperty(GlobalConstans.ALIYUN_PATH_ROOT, globalAliyunPathRoot);
				}
				if(!StringUtil.isEmptyString(globalAliyunBucket) || !StringUtil.isEmptyString(globalAliyunPathRoot)){
					SystemPropertiesUtil.writeProperties();
				}
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.left = new FormAttachment(100, -129);
		fd_btnNewButton.bottom = new FormAttachment(100, -55);
		fd_btnNewButton.right = new FormAttachment(100, -10);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("保   存");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
