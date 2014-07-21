package com.tools.song1.view.enjoy;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.constants.EnjoyConstans;
import com.tools.song1.constants.GlobalConstans;
import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.StringUtil;
import com.tools.song1.util.SystemPropertiesUtil;
import com.tools.song1.util.ViewFileDoUtil;
import com.tools.song1.view.MessageDialog;
import com.upload.aliyun.runnable.enjoy.EnjoyFileEachUtil;
import com.upload.aliyun.runnable.enjoy.EnjoyThread;
import com.upload.aliyun.runnable.enjoy.EnjoyToDoType;

public class EnjoyAllComposite extends Composite {
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private String ThreadRunnerName;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EnjoyAllComposite(Composite parent, int style) {
		super(parent, style);
		setSize(780, 300);
		setLayout(new FormLayout());
		Group group = new Group(this, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(0, 213);
		fd_group.right = new FormAttachment(0, 770);
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		group.setLayoutData(fd_group);
		group.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		group.setText("  设置   ");
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

		final Button btnNewButton_1 = new Button(group, SWT.NONE);
		GridData gd_btnNewButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_1.widthHint = 76;
		btnNewButton_1.setLayoutData(gd_btnNewButton_1);
		btnNewButton_1.setText("选择");
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String str = ViewFileDoUtil.folderDig(btnNewButton_1.getShell());
				if (!StringUtil.isEmptyString(str)) {
					text_3.setText(str);
				}
			}
		});

		Label lblNewLabel_4 = new Label(group, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("专辑风格根目录：");

		text_4 = new Text(group, SWT.BORDER);
		text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton = new Button(group, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 76;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("选择");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String str = ViewFileDoUtil.folderDig(btnNewButton_1.getShell());
				if (!StringUtil.isEmptyString(str)) {
					text_4.setText(str);
				}
			}
		});

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(null);
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(group, 0, SWT.RIGHT);
		fd_composite.bottom = new FormAttachment(group, 77, SWT.BOTTOM);
		fd_composite.top = new FormAttachment(group, 6);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);

		final Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.setBounds(383, 10, 174, 38);
		btnNewButton_2.addSelectionListener(new DoButtonStartSelectionAdapter());
		btnNewButton_2.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		btnNewButton_2.setText("开 始 上 传");
		
		Button btnNewButton_3 = new Button(composite, SWT.NONE);
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!StringUtil.isEmptyString(ThreadRunnerName)){
					GlobalConstans.THREAD_IS_RUN_MAPPING.put(ThreadRunnerName, false);
				}
			}
		});
		btnNewButton_3.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		btnNewButton_3.setBounds(576, 10, 174, 38);
		btnNewButton_3.setText("停 止 操 作");
		init();
	}

	public void init() {
		String enjoy_aliyun_bucket_all = SystemPropertiesUtil.getProperty(EnjoyConstans.ALIYUN_BUCKET_ALL);
		if (StringUtil.isEmptyString(enjoy_aliyun_bucket_all)) {
			enjoy_aliyun_bucket_all = SystemPropertiesUtil.getProperty(GlobalConstans.ALIYUN_BUCKET);
		}
		String enjoy_aliyun_bucket_path_root = SystemPropertiesUtil.getProperty(EnjoyConstans.ALIYUN_BASE_ROOT_ALL);
		if (StringUtil.isEmptyString(enjoy_aliyun_bucket_path_root)) {
			enjoy_aliyun_bucket_path_root = SystemPropertiesUtil.getProperty(GlobalConstans.ALIYUN_PATH_ROOT);
		}

		text.setText(enjoy_aliyun_bucket_all);
		text_1.setText(enjoy_aliyun_bucket_path_root);
		text_2.setText(SystemPropertiesUtil.getProperty(EnjoyConstans.SERVER_BASE_URL));
		text_3.setText(SystemPropertiesUtil.getProperty(EnjoyConstans.LOCAL_PATH_SINGERTYPE_ALL));
		text_4.setText(SystemPropertiesUtil.getProperty(EnjoyConstans.LOCAL_PATH_ALBUMSTYLE_ALL));
	}

	private class DoButtonStartSelectionAdapter extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if(!StringUtil.isEmptyString(ThreadRunnerName)){
				viewMessage("程序正在上传数据! ", null);
			}
			String bucket = text.getText();
			String pathRoot = text_1.getText();
			String serverPathRoot = text_2.getText();
			String singerType = text_3.getText();
			String albumStyle = text_4.getText();
			StringBuilder strb = new StringBuilder(100);
			boolean flag = true;
			strb.append("校验信息:\r\n");
			if (StringUtil.isEmptyString(bucket)) {
				flag = false;
				strb.append("请选输入阿里云BUCKET\r\n");
			}
			if (StringUtil.isEmptyString(pathRoot)) {
				flag = false;
				strb.append("请选输入阿里云根目录\r\n");
			}
			if (StringUtil.isEmptyString(serverPathRoot)) {
				flag = false;
				strb.append("请选输入阿里云根路径\r\n");
			}
			if (StringUtil.isEmptyString(singerType)) {
				flag = false;
				strb.append("请选择享CD的地域目录\r\n");
			}
			if (StringUtil.isEmptyString(albumStyle)) {
				flag = false;
				strb.append("请选择享CD的风格目录\r\n");
			}
			
			if (flag) {
				SystemPropertiesUtil.addProperty(EnjoyConstans.ALIYUN_BUCKET_ALL, bucket);
				SystemPropertiesUtil.addProperty(EnjoyConstans.ALIYUN_BASE_ROOT_ALL, pathRoot);
				SystemPropertiesUtil.addProperty(EnjoyConstans.SERVER_BASE_URL, serverPathRoot);
				SystemPropertiesUtil.addProperty(EnjoyConstans.LOCAL_PATH_SINGERTYPE_ALL, singerType);
				SystemPropertiesUtil.addProperty(EnjoyConstans.LOCAL_PATH_ALBUMSTYLE_ALL, albumStyle);
				SystemPropertiesUtil.writeProperties();
				EnjoyThread.ALIYUN_BUKET_NAME = bucket;
				EnjoyThread.ALIYUN_SERVER_PATH_ROOT = pathRoot;
				EnjoyThread.BASEPATH = serverPathRoot;
				
					EnjoyFileDoRunner runnable = new EnjoyFileDoRunner(singerType, albumStyle);
					Display.getDefault().syncExec(runnable);
					//new Thread(runnable).start();
					ThreadRunnerName = runnable.getThreadName();
					while(true){
						boolean runnableFlag = GlobalConstans.THREAD_IS_RUN_MAPPING.get(runnable.getThreadName());
						if(!runnableFlag){
							runnable.stopThread();
							break;
						}
					}
			}else{
				viewMessage("操作设置数据校验失败!", strb.toString());
			}
		}
	}

	private class EnjoyFileDoRunner implements Runnable {
		private String singerTypeFloder;
		private String albumStyleFloder;
		private String threadName;

		public EnjoyFileDoRunner(String singerTypeFloder, String albumStyleFloder) {
			threadName = Thread.currentThread().getName();
			GlobalConstans.THREAD_IS_RUN_MAPPING.put(threadName, true);
			this.singerTypeFloder = singerTypeFloder;
			this.albumStyleFloder = albumStyleFloder;
		}

		public String getThreadName() {
			return threadName;
		}

		@Override
		public void run() {
			FileDoUtil.outLog("开始处理地域..........................");
			EnjoyFileEachUtil.test(singerTypeFloder, EnjoyToDoType.SINGERTYPE);
			FileDoUtil.outLog("开始处理风格..........................");
			EnjoyFileEachUtil.test(albumStyleFloder, EnjoyToDoType.ALBUMSTYLE);
			viewMessage("上传成功! ", null);
		}
		
		public void stopThread(){
			Thread.currentThread().interrupt();
		}
	}

	private void viewMessage(String str, String detaile) {
		if (StringUtil.isEmptyString(str)) {
			return;
		}
		MessageDialog message = new MessageDialog(this.getShell(), SWT.NONE);
		message.open(str, detaile);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
