package com.tools.song1.view.song1.song;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.aliyun.view.AliyunFloderSelectDialog;
import com.tools.song1.runnable.enjoy.EnjoyFileEachUtil;
import com.tools.song1.runnable.enjoy.EnjoyThread;
import com.tools.song1.runnable.enjoy.EnjoyToDoType;
import com.tools.song1.util.StringUtil;
import com.tools.song1.util.ViewFileDoUtil;

public class EnjoyCDComposite extends Composite {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtNewText;
	private Text txtNewText_1;
	private Text text;
	private Text text_1;
	private Text text_2;
	private ProgressBar progressBar;
	private Text text_3;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EnjoyCDComposite(Composite parent, int style) {
		super(parent, style);
		setSize(740, 380);

		Group group = new Group(this, SWT.NONE);
		group.setText("   阿里云  设置   ");
		group.setBounds(10, 10, 720, 90);

		Label lblNewLabel = formToolkit.createLabel(group, "bucket：", SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel.setBounds(10, 25, 80, 17);

		txtNewText = formToolkit.createText(group, "New Text", SWT.READ_ONLY);
		txtNewText.setText("");
		txtNewText.setBounds(95, 22, 400, 23);

		Label lblNewLabel_1 = formToolkit.createLabel(group, "key：", SWT.NONE);
		lblNewLabel_1.setAlignment(SWT.RIGHT);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_1.setBounds(10, 60, 80, 17);

		txtNewText_1 = formToolkit.createText(group, "New Text", SWT.READ_ONLY);
		txtNewText_1.setText("");
		txtNewText_1.setBounds(95, 57, 400, 23);

		Button btnNewButton = formToolkit.createButton(group, "选  择", SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AliyunFloderSelectDialog d = new AliyunFloderSelectDialog(getShell(), SWT.NONE);
				Map<String, String> map = d.open();
				if (map != null) {
					txtNewText.setText(map.get("bucket") != null ? map.get("bucket") : "");
					txtNewText_1.setText(map.get("key") != null ? map.get("key") : "");
				}
				txtNewText_1.setFocus();
			}
		});
		btnNewButton.setBounds(505, 55, 80, 27);

		Group grpcd = new Group(this, SWT.NONE);
		grpcd.setText("   享CD   设置   ");
		grpcd.setBounds(10, 114, 720, 156);

		Label lblNewLabel_2 = formToolkit.createLabel(grpcd, "服务器URL：", SWT.NONE);
		lblNewLabel_2.setAlignment(SWT.RIGHT);
		lblNewLabel_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_2.setBounds(10, 25, 80, 17);

		Label lblNewLabel_3 = new Label(grpcd, SWT.NONE);
		lblNewLabel_3.setAlignment(SWT.RIGHT);
		lblNewLabel_3.setBounds(10, 55, 80, 17);
		lblNewLabel_3.setText("上传方式：");

		Button btnRadioButton = new Button(grpcd, SWT.RADIO);
		btnRadioButton.setSelection(true);
		btnRadioButton.setBounds(95, 55, 97, 17);
		btnRadioButton.setText("全部上传");

		Button btnRadioButton_1 = new Button(grpcd, SWT.RADIO);
		btnRadioButton_1.setBounds(197, 55, 97, 17);
		btnRadioButton_1.setText("按风格上传");

		Button btnRadioButton_2 = new Button(grpcd, SWT.RADIO);
		btnRadioButton_2.setBounds(319, 55, 97, 17);
		btnRadioButton_2.setText("按专辑上传");

		Label lblNewLabel_4 = new Label(grpcd, SWT.NONE);
		lblNewLabel_4.setAlignment(SWT.RIGHT);
		lblNewLabel_4.setBounds(10, 85, 80, 17);
		lblNewLabel_4.setText("地域选择：");

		Label label = new Label(grpcd, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setBounds(10, 120, 80, 17);
		label.setText("文件夹选择：");

		text = new Text(grpcd, SWT.BORDER);
		text.setBounds(96, 22, 400, 23);
		formToolkit.adapt(text, true, true);
		text.setText("http://localhost:8080/song1/api/enjoy/v1/");

		text_1 = new Text(grpcd, SWT.BORDER);
		text_1.setBounds(95, 82, 400, 23);
		formToolkit.adapt(text_1, true, true);

		text_2 = new Text(grpcd, SWT.BORDER);
		text_2.setBounds(95, 117, 400, 23);
		formToolkit.adapt(text_2, true, true);

		Button btnNewButton_1 = new Button(grpcd, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String str = ViewFileDoUtil.folderDig(getParent().getShell());
				if (!StringUtil.isEmptyString(str)) {
					text_1.setText(str);
				}
				text_1.setFocus();
			}
		});
		btnNewButton_1.setBounds(505, 80, 80, 27);
		formToolkit.adapt(btnNewButton_1, true, true);
		btnNewButton_1.setText("选  择");

		Button btnNewButton_2 = new Button(grpcd, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String str = ViewFileDoUtil.folderDig(getParent().getShell());
				if (!StringUtil.isEmptyString(str)) {
					text_2.setText(str);
				}
				text_2.setFocus();
			}
		});
		btnNewButton_2.setBounds(505, 115, 80, 27);
		formToolkit.adapt(btnNewButton_2, true, true);
		btnNewButton_2.setText("选  择");

		final Button btnNewButton_3 = new Button(this, SWT.NONE);
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String bucket = txtNewText.getText();
				String key = txtNewText_1.getText();
				String url = text.getText();
				String singerTypePath = text_1.getText();
				String stylePath = text_2.getText();
				if (StringUtil.isEmptyString(bucket)) {
					message("请选择阿里云bucket");
				} else if (key == null) {
					message("请选择阿里云key");
				} else if (StringUtil.isEmptyString(url)) {
					message("请输入服务器保存享CD数据的url");
				} else if (StringUtil.isEmptyString(singerTypePath)) {
					message("请选择地域文件夹");
				} else if (StringUtil.isEmptyString(stylePath)) {
					message("请选择风格文件夹");
				}else{
					text_3.setVisible(false);
					UploadEnjoyDataThread doEnjoy = new UploadEnjoyDataThread(bucket, key, url, singerTypePath, stylePath);
					new Thread(doEnjoy).start();
					progressBar.setVisible(true);
				}
			}
		});
		btnNewButton_3.setBounds(612, 302, 118, 33);
		formToolkit.adapt(btnNewButton_3, true, true);
		btnNewButton_3.setText("开 始 上 传");

		text_3 = new Text(this, SWT.READ_ONLY | SWT.WRAP | SWT.CENTER | SWT.MULTI);
		text_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		text_3.setBounds(20, 300, 546, 23);
		text_3.setVisible(false);

		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setBounds(10, 353, 720, 17);
		formToolkit.adapt(progressBar, true, true);
		progressBar.setSelection(0);
		progressBar.setVisible(false);

	}

	private void message(String str) {
		if (StringUtil.isEmptyString(str)) {
			return;
		}
		text_3.setVisible(true);
		text_3.setText(str);
		text_3.update();
	}

	private class UploadEnjoyDataThread implements Runnable {
		private String bucket;
		private String key;
		private String url;
		private String singerTypePath;
		private String stylePath;

		public UploadEnjoyDataThread(String bucket, String key, String url, String singerTypePath, String stylePath) {
			this.bucket = bucket;
			this.key = key;
			this.url = url;
			this.singerTypePath = singerTypePath;
			this.stylePath = stylePath;
		}

		public void run() {
			EnjoyFileEachUtil.progressBar = progressBar;
			EnjoyThread.ALIYUN_BUKET_NAME = bucket;
			EnjoyThread.ALIYUN_SERVER_PATH_ROOT = key;
			EnjoyThread.BASEPATH = url;
			EnjoyFileEachUtil.test(singerTypePath, EnjoyToDoType.SINGERTYPE);
			EnjoyFileEachUtil.test(stylePath, EnjoyToDoType.ALBUMSTYLE);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					progressBar.setSelection(0);
					progressBar.setVisible(false);
					message("数据上传完成!");
				}
			});
		}

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
