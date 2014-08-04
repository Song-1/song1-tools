package com.tools.song1.aliyun.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.aliyun.openservices.ClientException;
import com.tools.song1.aliyun.UploadFiles;
import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.LayoutUtil;
import com.tools.song1.util.StringUtil;
import org.eclipse.swt.widgets.Scale;

public class UploadFilesDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Map<String, String> parentParamMap = new HashMap<String, String>();
	private Button btnNewButton;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private ProgressBar progressBar;
	private ProgressBar progressBar_1;
	private Button btnNewButton_1;
	private Text txtNewText;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public UploadFilesDialog(Shell parent, int style) {
		super(parent, style);
		parent.setEnabled(false);
		setText("SWT Dialog");
	}

	public Map<String, String> getParentParamMap() {
		return parentParamMap;
	}

	public void setParentParamMap(Map<String, String> parentParamMap) {
		this.parentParamMap = parentParamMap;
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
				shell.getParent().setEnabled(true);
			}
		});
		shell.setSize(722,600);
		shell.setText("文件上传");

		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(0, 80, 706, 476);

		Composite composite = new Composite(composite_1, SWT.NONE);
		composite.setLocation(5, 260);
		composite.setSize(701, 65);

		progressBar = new ProgressBar(composite, SWT.NONE);
		progressBar.setLocation(63, 10);
		progressBar.setSize(628, 17);
		// progressBar.setSelection(34);
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(0, 10, 50, 17);
		lblNewLabel.setText("进度：");

		progressBar_1 = new ProgressBar(composite, SWT.NONE);
		progressBar_1.setBounds(63, 38, 628, 17);
		formToolkit.adapt(progressBar_1, true, true);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		label_1.setBounds(0, 38, 61, 17);
		//formToolkit.adapt(label_1, true, true);
		label_1.setText("文件进度：");

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setBounds(0, 331, 706, 136);

		text = new Text(composite_2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text.setBounds(5, 10, 696, 118);

		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setBounds(0, 0, 706, 254);

		btnNewButton_1 = new Button(composite_3, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String bucket = text_2.getText();
				String key = (String) text_3.getData();
				String filePath = text_1.getText();
				// Display.getDefault().syncExec(new UploadFilesRunnable(bucket,
				// key, filePath));
				UploadFilesRunnable r = new UploadFilesRunnable(bucket, key, filePath);
				new Thread(r).start();
			}
		});
		btnNewButton_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 11, SWT.NORMAL));
		btnNewButton_1.setBounds(253, 207, 188, 37);
		btnNewButton_1.setText("开  始  上  传");

		Label label = new Label(composite_3, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 33, 706, 2);

		Label lblNewLabel_2 = new Label(composite_3, SWT.NONE);
		lblNewLabel_2.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel_2.setBounds(10, 0, 277, 33);
		lblNewLabel_2.setText("上传文件到阿里云：");

		Group group = new Group(composite_3, SWT.NONE);
		group.setBounds(10, 112, 686, 89);
		final Button btnRadioButton = new Button(group, SWT.RADIO);
		btnRadioButton.setSelection(true);
		btnRadioButton.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		btnRadioButton.setBounds(77, 20, 97, 17);
		btnRadioButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		//formToolkit.adapt(btnRadioButton, true, true);
		btnRadioButton.setText("单文件上传");

		Label lblNewLabel_5 = formToolkit.createLabel(group, "上传方式：", SWT.NONE);
		lblNewLabel_5.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_5.setBounds(10, 20, 61, 17);

		final Button btnRadioButton_1 = new Button(group, SWT.RADIO);
		btnRadioButton_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		btnRadioButton_1.setBounds(183, 20, 97, 17);
		btnRadioButton_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		//formToolkit.adapt(btnRadioButton_1, true, true);
		btnRadioButton_1.setText("按文件夹上传");

		text_1 = new Text(group, SWT.BORDER | SWT.READ_ONLY);
		text_1.setLocation(77, 54);
		text_1.setSize(490, 25);

		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setBounds(10, 59, 65, 20);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_1.setText("选择文件：");

		btnNewButton = new Button(group, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String str = null;
				if (btnRadioButton.getSelection()) {
					str = fileDig(shell);
					if (StringUtil.isEmptyString(str)) {
						new ErrorDialog(shell, SWT.NONE).open("请选择要上传的文件");
						str = "";
					}
				} else if (btnRadioButton_1.getSelection()) {
					str = folderDig(shell);
					if (StringUtil.isEmptyString(str)) {
						new ErrorDialog(shell, SWT.NONE).open("请选择要上传的文件夹");
						str = "";
					}
					str += File.separator;
				}
				text_1.setText(str);
				text_1.update();
			}
		});
		btnNewButton.setBounds(573, 53, 107, 27);
		btnNewButton.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		btnNewButton.setText("选   择");

		Group group_1 = new Group(composite_3, SWT.NONE);
		group_1.setBounds(10, 33, 686, 89);

		Label lblNewLabel_3 = new Label(group_1, SWT.NONE);
		lblNewLabel_3.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_3.setBounds(10, 22, 61, 17);
		lblNewLabel_3.setText("BUCKET:");

		text_2 = new Text(group_1, SWT.BORDER | SWT.READ_ONLY);
		text_2.setBounds(77, 20, 490, 23);

		Label lblNewLabel_4 = new Label(group_1, SWT.NONE);
		lblNewLabel_4.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_4.setBounds(10, 55, 61, 17);
		lblNewLabel_4.setText("上传路径：");

		text_3 = new Text(group_1, SWT.BORDER | SWT.READ_ONLY);
		text_3.setBounds(77, 52, 490, 23);
		
		Composite composite_4 = formToolkit.createComposite(shell, SWT.NONE);
		composite_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite_4.setBounds(0, 10, 696, 64);
		formToolkit.paintBordersFor(composite_4);
		
		Label lblNewLabel_6 = formToolkit.createLabel(composite_4, "遍历文件开始的位置：", SWT.NONE);
		lblNewLabel_6.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_6.setBounds(10, 10, 129, 17);
		
		txtNewText = formToolkit.createText(composite_4, "New Text", SWT.NONE);
		txtNewText.setText("0");
		txtNewText.setBounds(139, 9, 258, 23);
		init();
	}

	private void init() {
		String bucket = getMapValue("bucket");
		String key = getMapValue("key");
		if ("".equals(key)) {
			text_3.setText("根路径");
			text_3.setData("");
		} else {
			text_3.setText(key);
			text_3.setData(key);
		}
		text_2.setText(bucket);
		text_2.update();
		text_3.update();
	}

	private String getMapValue(String key) {
		if (parentParamMap != null) {
			String str = parentParamMap.get(key);
			if (str == null) {
				return "";
			} else {
				return str;
			}
		}
		return "";
	}

	/**
	 * 文件夹（目录）选择对话框
	 */
	public static String folderDig(Shell parent) {
		// 新建文件夹（目录）对话框
		DirectoryDialog folderdlg = new DirectoryDialog(parent);
		// 设置文件对话框的标题
		folderdlg.setText("文件选择");
		// 设置初始路径
		folderdlg.setFilterPath("SystemDrive");
		// 设置对话框提示文本信息
		folderdlg.setMessage("请选择相应的文件夹");
		// 打开文件对话框，返回选中文件夹目录
		String selecteddir = folderdlg.open();
		if (selecteddir == null) {
			return null;
		} else {
			FileDoUtil.outLog("您选中的文件夹目录为：" + selecteddir);
			return selecteddir;
		}
	}

	/**
	 * 文件夹（目录）选择对话框
	 */
	public static String fileDig(Shell parent) {
		// 新建文件夹（目录）对话框
		FileDialog folderdlg = new FileDialog(parent);
		// 设置文件对话框的标题
		folderdlg.setText("文件选择");
		// 设置初始路径
		folderdlg.setFilterPath("SystemDrive");
		// 设置对话框提示文本信息
		// folderdlg.setMessage("请选择相应的文件夹");
		// 打开文件对话框，返回选中文件夹目录
		String selecteddir = folderdlg.open();
		while (true) {
			if (selecteddir == null) {
				return null;
			} else {
				if (!StringUtil.isEmptyString(selecteddir)) {
					File file = new File(selecteddir);
					if (file.exists()) {
						FileDoUtil.outLog("您选中的文件夹目录为：" + selecteddir);
						return selecteddir;
					} else {
						new ErrorDialog(parent, SWT.NONE).open("所选文件不存在,请重新选择");
						selecteddir = folderdlg.open();
					}
				}

			}
		}
	}

	private void outLogToText(String str) {
		final String value = str;
		if (!StringUtil.isEmptyString(value)) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					text.append(value + "\r\n");
					text.update();
				}
			});
		}
	}

	private class UploadFilesRunnable implements Runnable {
		private String bucket;
		private String baseKey;
		private String baseFilePath;
		private int START = 0;

		public UploadFilesRunnable(String bucket, String baseKey, String baseFilePath) {
			this.bucket = bucket;
			this.baseKey = baseKey;
			this.baseFilePath = baseFilePath;
		}

		@Override
		public void run() {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					String str = txtNewText.getText();
					if(StringUtil.isEmptyString(str)){
						START = 0;
					}else{
						START = Integer.parseInt(str);
					}
					text.setText("");
					text.update();
					btnNewButton.setEnabled(false);
					btnNewButton_1.setEnabled(false);
				}
			});
			File file = new File(baseFilePath);
			eachFiles(file, true);
			System.out.println(FILE_COUNT);
			eachFiles(file, false);
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					btnNewButton.setEnabled(true);
					btnNewButton_1.setEnabled(true);
				}
			});
		}

		private int FILE_COUNT = 1;
		private int count = 1;

		private void eachFiles(File file, boolean isCount) {
			if (file != null && file.exists()) {
				if (file.isDirectory()) {
					File[] files = file.listFiles();
					if (files != null) {
						for (File f : files) {
							eachFiles(f, isCount);
						}
					}
				} else if (file.isFile()) {
					if (isCount) {
						FILE_COUNT++;
					} else {
						count++;
						if(START > count && START <  FILE_COUNT){
							printProgress(count, FILE_COUNT);
							return;
						}
						String key = file.getAbsolutePath();
						if (key.equals(baseFilePath)) {
							key = file.getName();
						} else {
							key = key.replace(baseFilePath, "");
						}
						key = baseKey + key;
						key = key.replace(File.separator, "/");
						outLogToText(key);
						upload(key, file, 0);
						printProgress(count, FILE_COUNT);
					}
				}
			}
		}
		
		private void printProgress(int currentNUmbers,int countNumbers){
			final int value = (currentNUmbers * 100 / countNumbers);
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					progressBar.setSelection(value);
					progressBar.update();
				}
			});
		}

		private void upload(String key, File file, int counts) {
			counts++;
			try {
				UploadFiles uploadFiles = new UploadFiles();
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						progressBar_1.setSelection(1);
						progressBar_1.update();
					}
				});
				uploadFiles.setProgressBar(progressBar_1);
				uploadFiles.uploadBigFile(bucket, key, file);
				System.out.println(count);
				// OSSUploadUtil.uploadBigFile(bucket, key, file);
			} catch (ClientException e) {
				outLogToText("[网络连接失败][上传文件失败] 文件:" + file.getAbsolutePath());
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				outLogToText("第" + counts + " 次 重连上传 ");
				if (counts < 10) {
					upload(key, file, counts);
				}
			} catch (Exception e) {
				// FileDoUtil.outLog("");
				outLogToText("[上传文件失败] 文件:" + file.getAbsolutePath());
				e.printStackTrace();
			}
		}

	}
}
