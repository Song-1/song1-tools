package com.tools.song1.view.enjoy;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.constants.EnjoyConstans;
import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.StringUtil;
import com.tools.song1.util.SystemPropertiesUtil;
import com.tools.song1.util.ViewFileDoUtil;
import com.tools.song1.view.BaseComposite;
import com.upload.aliyun.runnable.enjoy.EnjoyThread;

public class AllSvaeComposite extends BaseComposite {
	protected Composite shlcd;
	private Text text;
	private Text text_1;
	private Text txtMessage;
	private Text text_3;
	private Text text_4;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public AllSvaeComposite(Composite parent, int style) {
		super(parent, style);
		shlcd = this;
		setSize(780, 400);
		setLayout(new GridLayout(1, true));

		Group group_2 = new Group(shlcd, SWT.NONE);
		group_2.setText("设置阿里云信息");
		GridData gd_group_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_2.heightHint = 80;
		gd_group_2.widthHint = 762;
		group_2.setLayoutData(gd_group_2);

		Label lblNewLabel_3 = new Label(group_2, SWT.NONE);
		lblNewLabel_3.setBounds(10, 25, 61, 17);
		lblNewLabel_3.setText("BUCKET：");

		Label lblNewLabel_4 = new Label(group_2, SWT.NONE);
		lblNewLabel_4.setBounds(10, 64, 92, 17);
		lblNewLabel_4.setText("根路径：");

		text_3 = new Text(group_2, SWT.BORDER);
		text_3.setBounds(108, 22, 505, 23);
		text_3.setText(SystemPropertiesUtil.getProperty(EnjoyConstans.ALIYUN_BUCKET_ALL));

		text_4 = new Text(group_2, SWT.BORDER);
		text_4.setBounds(108, 61, 505, 23);
		text_4.setText(SystemPropertiesUtil.getProperty(EnjoyConstans.ALIYUN_BASE_ROOT_ALL));
		Group grpSingertype = new Group(shlcd, SWT.NONE);
		GridData gd_grpSingertype = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpSingertype.heightHint = 55;
		gd_grpSingertype.widthHint = 762;
		grpSingertype.setLayoutData(gd_grpSingertype);
		grpSingertype.setText("歌手及所属地域信息");

		text = new Text(grpSingertype, SWT.BORDER);
		text.setEditable(false);
		text.setBounds(108, 27, 417, 23);
		text.setText(SystemPropertiesUtil.getProperty(EnjoyConstans.LOCAL_PATH_SINGERTYPE_ALL));

		Label lblNewLabel = new Label(grpSingertype, SWT.NONE);
		lblNewLabel.setBounds(10, 30, 92, 17);
		lblNewLabel.setText("选择地域目录：");

		Button btnNewButton = new Button(grpSingertype, SWT.NONE);
		btnNewButton.setBounds(531, 25, 80, 27);
		btnNewButton.setText("选择");
		// /// event
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String singerTypeFloder = ViewFileDoUtil.folderDig(shlcd.getShell());
				if (!StringUtil.isEmptyString(singerTypeFloder)) {
					text.setText(singerTypeFloder);
				}
			}
		});

		Group group = new Group(shlcd, SWT.NONE);
		group.setText("专辑风格和专辑数据");
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group.heightHint = 55;
		gd_group.widthHint = 762;
		group.setLayoutData(gd_group);

		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setBounds(10, 32, 92, 17);
		lblNewLabel_1.setText("选择风格目录：");

		text_1 = new Text(group, SWT.BORDER);
		text_1.setEditable(false);
		text_1.setBounds(108, 26, 419, 23);
		text_1.setText(SystemPropertiesUtil.getProperty(EnjoyConstans.LOCAL_PATH_ALBUMSTYLE_ALL));

		Button btnNewButton_1 = new Button(group, SWT.NONE);
		btnNewButton_1.setBounds(533, 27, 80, 27);
		btnNewButton_1.setText("选择");
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String albumStyleFloder = ViewFileDoUtil.folderDig(shlcd.getShell());
				if (!StringUtil.isEmptyString(albumStyleFloder)) {
					text_1.setText(albumStyleFloder);
				}
			}
		});

		txtMessage = new Text(shlcd, SWT.FILL | SWT.WRAP | SWT.CENTER);
		txtMessage.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtMessage.setEditable(false);
		// txtNihao.setText("nihao");
		txtMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		txtMessage.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		GridData gd_txtMessage = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_txtMessage.heightHint = 25;
		gd_txtMessage.widthHint = 665;
		txtMessage.setLayoutData(gd_txtMessage);

	    Button btnNewButton_2 = new Button(shlcd, SWT.NONE);
		btnNewButton_2.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		GridData gd_btnNewButton_2 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_2.heightHint = 50;
		gd_btnNewButton_2.widthHint = 199;
		btnNewButton_2.setLayoutData(gd_btnNewButton_2);
		btnNewButton_2.setText("开始处理");
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				txtMessage.setText("");
				Map<String, Object> map = getParentDataMap();
				if (map == null) {
					txtMessage.setText("请选输入服务器基路径");
				}
				String baseUrl = (String) map.get("enjoy.server.base.url");
				String singerTypeFloder = text.getText();
				String albumStyleFloder = text_1.getText();
				String aliyunBucket = text_3.getText();
				String aliyunBaseRoot = text_4.getText();
				if (StringUtil.isEmptyString(baseUrl)) {
					txtMessage.setText("请选输入服务器基路径");
				} else if (StringUtil.isEmptyString(aliyunBucket)) {
					txtMessage.setText("请选输入阿里云BUCKET");
				} else if (StringUtil.isEmptyString(aliyunBaseRoot)) {
					txtMessage.setText("请选输入阿里云根路径");
				} else if (StringUtil.isEmptyString(singerTypeFloder)) {
					txtMessage.setText("请选择享CD的地域目录");
				} else if (StringUtil.isEmptyString(albumStyleFloder)) {
					txtMessage.setText("请选择享CD的风格目录");
				} else {
					SystemPropertiesUtil.addProperty(EnjoyConstans.ALIYUN_BUCKET_ALL, aliyunBucket);
					SystemPropertiesUtil.addProperty(EnjoyConstans.ALIYUN_BASE_ROOT_ALL, aliyunBaseRoot);
					SystemPropertiesUtil.addProperty(EnjoyConstans.SERVER_BASE_URL, baseUrl);
					SystemPropertiesUtil.addProperty(EnjoyConstans.LOCAL_PATH_SINGERTYPE_ALL, singerTypeFloder);
					SystemPropertiesUtil.addProperty(EnjoyConstans.LOCAL_PATH_ALBUMSTYLE_ALL, albumStyleFloder);
					SystemPropertiesUtil.writeProperties();
					EnjoyThread.ALIYUN_BUKET_NAME = aliyunBucket;
					EnjoyThread.ALIYUN_SERVER_PATH_ROOT = aliyunBaseRoot;
					EnjoyThread.BASEPATH = baseUrl;
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							txtMessage.setText("正在上传享CD文件,请等待...............");
						}
					});
					EnjoyFileDoRunner runnable = new EnjoyFileDoRunner(singerTypeFloder, albumStyleFloder);
					Display.getDefault().syncExec(runnable);
					//new Thread().start();
				}
			}
		});
	}
	

	private class EnjoyFileDoRunner implements Runnable {
		private String singerTypeFloder;
		private String albumStyleFloder;

		public EnjoyFileDoRunner(String singerTypeFloder, String albumStyleFloder) {
			this.singerTypeFloder = singerTypeFloder;
			this.albumStyleFloder = albumStyleFloder;
		}
		@Override
		public void run() {
			FileDoUtil.outLog("处理地域..........................");
			//EnjoyFileEachUtil.test(singerTypeFloder, EnjoyToDoType.SINGERTYPE);
			FileDoUtil.outLog("处理风格..........................");
			//EnjoyFileEachUtil.test(albumStyleFloder, EnjoyToDoType.ALBUMSTYLE);
			txtMessage.setText("上传成功!");
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
