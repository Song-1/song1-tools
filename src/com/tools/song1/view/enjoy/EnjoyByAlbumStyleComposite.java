package com.tools.song1.view.enjoy;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

public class EnjoyByAlbumStyleComposite extends Composite {
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EnjoyByAlbumStyleComposite(Composite parent, int style) {
		super(parent, style);
		setSize(780,300);
		setLayout(new FormLayout());
		
		Group group = new Group(this, SWT.NONE);
		group.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		group.setText("  设置   ");
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(0, 217);
		fd_group.right = new FormAttachment(0, 770);
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		group.setLayoutData(fd_group);
		group.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("阿里云BUCKET：");
		
		text = new Text(group, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("阿里云根路径：");
		
		text_1 = new Text(group, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group, SWT.NONE);
		
		Label lblNewLabel_2 = new Label(group, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("服务器根路径：");
		
		text_2 = new Text(group, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group, SWT.NONE);
		
		Label lblNewLabel_3 = new Label(group, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("歌手地域根目录：");
		
		text_3 = new Text(group, SWT.BORDER);
		text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton_1 = new Button(group, SWT.NONE);
		GridData gd_btnNewButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_1.widthHint = 76;
		btnNewButton_1.setLayoutData(gd_btnNewButton_1);
		btnNewButton_1.setText("选择");
		
		Label lblNewLabel_4 = new Label(group, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("专辑风格目录：");
		
		text_4 = new Text(group, SWT.BORDER);
		text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton = new Button(group, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 76;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("选择");
		
		Button btnNewButton_2 = new Button(this, SWT.NONE);
		btnNewButton_2.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		FormData fd_btnNewButton_2 = new FormData();
		fd_btnNewButton_2.top = new FormAttachment(group, 17);
		fd_btnNewButton_2.bottom = new FormAttachment(100, -28);
		fd_btnNewButton_2.left = new FormAttachment(0, 596);
		fd_btnNewButton_2.right = new FormAttachment(100, -10);
		btnNewButton_2.setLayoutData(fd_btnNewButton_2);
		btnNewButton_2.setText("开 始 上 传");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
