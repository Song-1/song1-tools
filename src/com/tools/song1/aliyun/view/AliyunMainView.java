package com.tools.song1.aliyun.view;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.runnable.aliyun.ListAliyunFilesRunnable;
import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.JavascriptUtil;
import com.tools.song1.util.LayoutUtil;
import com.tools.song1.util.StringUtil;
import com.upload.aliyun.MusicConstants;

public class AliyunMainView {

	protected Shell shell;
	private Table table;
	private Tree tree;
	private Label lblNewLabel;
	private Composite composite_1;
	private Composite composite_2;
	private Text text;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Label lblNewLabel_4;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(".............加载配置文件并初始化...................");
			// /// 加载配置文件
			MusicConstants.loadConfig();
			OSSUploadUtil.init();
			JavascriptUtil.init();
			System.out.println("................初始化 完成...................");
			//
			AliyunMainView window = new AliyunMainView();
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
		LayoutUtil.centerShell(display, shell);
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
		shell.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/song1.png"));
		shell.setSize(1000, 600);
		shell.setText("阿里云文件处理器");
		shell.setLayout(null);

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLocation(10, 40);
		composite.setSize(191, 505);
//		composite.setBounds(10, 40, 191, 505);

		tree = new Tree(composite, SWT.NONE);
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = tree.getSelection()[0];
				if (item != null) {
					String bucket = item.getText();
					if (!StringUtil.isEmptyString(bucket)) {
						lblNewLabel.setText(bucket);
						lblNewLabel.update();
						// loadingImage();
						newListAliyunFilesRunnable(bucket, "");
					}
				}
			}
		});
		tree.setBounds(-30, 0, 217, 500);
		
//		TreeItem trtmNewTreeitem_1 = new TreeItem(tree, SWT.NONE);
//		trtmNewTreeitem_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
//		trtmNewTreeitem_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
//		trtmNewTreeitem_1.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/bucket4.png"));
//		trtmNewTreeitem_1.setText("New TreeItem");
		
		composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(210, 51, 775, 499);
		composite_2 = new Composite(composite_1, SWT.NONE);

		composite_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		composite_2.setLocation(364, 204);
		composite_2.setSize(32, 32);
		composite_2.setVisible(false);
		Label label_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(210, 43, 774, 2);

		Label label = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label.setBounds(207, 0, 4, 570);

		lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel.setBounds(220, 10, 400, 30);

		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel_1.setBounds(10, 10, 191, 25);
		lblNewLabel_1.setText("阿里云BUCKET：");

		table = new Table(composite_1, SWT.NONE);
		table.addMouseListener(new TableMouseAdapter());

		table.setBounds(0, 80, 772, 372);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
		tableColumn.setWidth(666);
		tableColumn.setText("文件名称");
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		tableItem.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/folder.png"));
		tableItem.setText("  sgsdfg  ");
		
		Label label_3 = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_3.setBounds(0, 492, 772, 2);
		
		text = new Text(composite_1, SWT.READ_ONLY);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text.setBounds(0, 470, 772, 23);
		
		Label label_2 = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBounds(0, 468, 772, 2);
		
		lblNewLabel_4 = formToolkit.createLabel(composite_1, "New Label", SWT.NONE);
		lblNewLabel_4.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_4.setBounds(116, 53, 634, 20);
		
		Label lblNewLabel_3 = formToolkit.createLabel(composite_1, "当前文件夹：", SWT.NONE);
		lblNewLabel_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_3.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		lblNewLabel_3.setBounds(32, 53, 78, 20);
		
		Label lblNewLabel_2 = formToolkit.createLabel(composite_1, "", SWT.NONE);
		lblNewLabel_2.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/folder.png"));
		lblNewLabel_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_2.setBounds(10, 51, 26, 23);
		
		Label label_4 = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_4.setBounds(0, 45, 772, 2);
		formToolkit.adapt(label_4, true, true);
		
//		Button btnNewButton = new Button(composite_1, SWT.NONE);
//		btnNewButton.setLocation(232, 0);
//		btnNewButton.setSize(132, 39);
//		btnNewButton.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/new.png"));
//		formToolkit.adapt(btnNewButton, true, true);
//		btnNewButton.setText("新建文件夹");
		
		Button btnNewButton_1 = new Button(composite_1, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		btnNewButton_1.setBounds(126, 0, 100, 39);
		btnNewButton_1.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/refresh_bucket.png"));
		formToolkit.adapt(btnNewButton_1, true, true);
		btnNewButton_1.setText("刷新");
		
		Button btnNewButton_2 = formToolkit.createButton(composite_1, "返回上级", SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String key = lblNewLabel_4.getText();
				if("根目录".equals(key)){
					new ErrorDialog(shell, SWT.NONE).open("当前目录已经是根目录");
				}else if(!StringUtil.isEmptyString(key)){
					if(key.endsWith("/")){
						key = new String(key.substring(0,key.length() - 1 ));
					}
					int i = key.lastIndexOf("/");
					if(i < 0){
						key = "";
					}else{
						key = new String(key.substring(0,i+1));
					}
					String bucket = lblNewLabel.getText().trim();
					if (key != null && !StringUtil.isEmptyString(bucket)) {
						newListAliyunFilesRunnable(bucket, key);
					}
				}
			}
		});
		btnNewButton_2.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/up.png"));
		btnNewButton_2.setBounds(0, 0, 120, 39);
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String,String> params = new HashMap<String, String>();
				String bucket = lblNewLabel.getText();
				String key = lblNewLabel_4.getText();
				if(StringUtil.isEmptyString(bucket)){
					new ErrorDialog(shell, SWT.NONE).open("阿里云BUCKET不能为空");
				}else if(StringUtil.isEmptyString(key)){
					new ErrorDialog(shell, SWT.NONE).open("当前目录不能为空");
				}else {
					if("根目录".equals(key)){key = "";}
					params.put("bucket", bucket);
					params.put("key", key);
					UploadFilesDialog uploadFilesDialog = new UploadFilesDialog(shell, SWT.NONE);
					uploadFilesDialog.setParentParamMap(params);
					uploadFilesDialog.open();
				}
			}
		});
		btnNewButton.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/upload.png"));
		btnNewButton.setBounds(232, 0, 120, 39);
		formToolkit.adapt(btnNewButton, true, true);
		btnNewButton.setText("文件上传");
		init();

	}

	private void init() {
		List<String> buckets = OSSUploadUtil.listAllBucketName();
		// String selectBucket = null;
		if (buckets != null && !buckets.isEmpty()) {
			int namesize = 25;
			// selectBucket = buckets.get(0);
			for (String bucket : buckets) {
				TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
				trtmNewTreeitem.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				trtmNewTreeitem.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
				trtmNewTreeitem.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/bucket4.png"));
				if (bucket.length() < namesize) {
					for (int i = bucket.length(); i < namesize; i++)
						bucket += " ";
				}
				trtmNewTreeitem.setText(bucket);
			}
		}
	}

	public void loadingImage() {
		composite_2.setVisible(true);
		ImageViewer ic = new ImageViewer(composite_2);
		ImageLoader loader = new ImageLoader();
		File file = FileDoUtil.findFile("images/loading.gif");
		ImageData[] imageDatas = loader.load(file.getAbsolutePath());
		if (imageDatas.length == 0)
			return;
		else if (imageDatas.length == 1) {
			ic.setImage(imageDatas[0]);
		} else {
			ic.setImages(imageDatas, loader.repeatCount);
		}
		ic.pack();
	}

	/**
	 * 新建表格的右键菜单
	 * 
	 * @return
	 */
	private Menu getTableMenu(boolean isFloder) {
		Menu menu = new Menu(table);
		if (isFloder) {
			MenuItem menuItem = new MenuItem(menu, SWT.NONE);
			menuItem.setText("打开");
			menuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					tableSelectToDo();
				}
			});
		}
		MenuItem mntmTest = new MenuItem(menu, SWT.NONE);
		mntmTest.setText("修改名称");
		mntmTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				if (items != null && items.length > 0) {
					TableItem tableItem = items[0];
					if (tableItem != null) {
						String key = (String) tableItem.getData();
						String bucket = lblNewLabel.getText();
						if (key != null && !StringUtil.isEmptyString(bucket)) {
							ModiyFileNameDialog modiyFileNameDialog = new ModiyFileNameDialog(shell, SWT.NONE);
							modiyFileNameDialog.setBucket(bucket);
							modiyFileNameDialog.setTableItem(tableItem);
							modiyFileNameDialog.open();
						}
					}
				}
			}
		});
		MenuItem mntmNewItem1 = new MenuItem(menu, SWT.NONE);
		mntmNewItem1.setText("复制到");
		mntmNewItem1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				if (items != null && items.length > 0) {
					TableItem tableItem = items[0];
					if (tableItem != null) {
						String key = (String) tableItem.getData();
						String bucket = lblNewLabel.getText();
						if (key != null && !StringUtil.isEmptyString(bucket)) {
							Map<String,String> params = new HashMap<String, String>();
							params.put("sourceBucketName", bucket);
							params.put("sourcekey", key);
							params.put("sourceName", tableItem.getText());
							params.put("doFlag", "copyTo");
							AliyunFileSelectDialog aliyunFileSelectDialog = new AliyunFileSelectDialog(shell, SWT.NONE,params);
							aliyunFileSelectDialog.open();
						}
					}
				}
			}
		});
		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.setText("移动到");
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				if (items != null && items.length > 0) {
					TableItem tableItem = items[0];
					if (tableItem != null) {
						String key = (String) tableItem.getData();
						String bucket = lblNewLabel.getText();
						if (key != null && !StringUtil.isEmptyString(bucket)) {
							Map<String,String> params = new HashMap<String, String>();
							params.put("sourceBucketName", bucket);
							params.put("sourcekey", key);
							params.put("sourceName", tableItem.getText());
							params.put("doFlag", "cmoveTo");
							AliyunFileSelectDialog aliyunFileSelectDialog = new AliyunFileSelectDialog(shell, SWT.NONE,params);
							aliyunFileSelectDialog.open();
						}
					}
				}
			}
		});
		MenuItem mntmNewItem2 = new MenuItem(menu, SWT.NONE);
		mntmNewItem2.setText("删除");
		mntmNewItem2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				if (items != null && items.length > 0) {
					TableItem tableItem = items[0];
					if (tableItem != null) {
						String key = (String) tableItem.getData();
						String bucket = lblNewLabel.getText();
						if (key != null && !StringUtil.isEmptyString(bucket)) {
							OSSUploadUtil.deleteKey(bucket, key);
							new ErrorDialog(shell, SWT.NONE).open("删除成功");
							refresh();
						}
					}
				}
			}
		});
		return menu;
	}

	private class TableMouseAdapter extends MouseAdapter {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			System.out.println("double clicke...........");
			tableSelectToDo();
		}

		public void mouseDown(MouseEvent e) {
			if (e.button == 3) {
				TableItem[] items = table.getSelection();
				if (items != null && items.length > 0) {
					TableItem tableItem = items[0];
					if (tableItem != null) {
						String key = (String) tableItem.getData();
						Menu menu = null;
						if (key == null || (!StringUtil.isEmptyString(key) && !key.endsWith("/"))) {
							menu = getTableMenu(false);
						} else {
							menu = getTableMenu(true);
						}
						table.setMenu(menu);
					}
				}
			}
		}
	}

	private void tableSelectToDo() {
		TableItem[] items = table.getSelection();
		if (items != null && items.length > 0) {
			TableItem tableItem = items[0];
			if (tableItem != null) {
				String key = (String) tableItem.getData();
				if (key == null || (!StringUtil.isEmptyString(key) && !key.endsWith("/"))) {
					return;
				}
				String bucket = lblNewLabel.getText();
				if (key != null && !StringUtil.isEmptyString(bucket)) {
					System.out.println(key);
					newListAliyunFilesRunnable(bucket, key);
				}
			}
		}
	}

	private void newListAliyunFilesRunnable(String bucket, String key) {
		String keyStr = key;
		if("".equals(keyStr)){
			keyStr = "根目录";
		}
		text.setText("正在加载文件:::[bucket="+bucket.trim()+";key="+keyStr+"]");
		text.update();
		lblNewLabel_4.setText(keyStr);
		lblNewLabel_4.update();
		ListAliyunFilesRunnable listAliyunFilesRunnable = new ListAliyunFilesRunnable(table, bucket, key);
		Display.getCurrent().syncExec(listAliyunFilesRunnable);
	}
	
	private void refresh(){
		String key = lblNewLabel_4.getText();
		if("根目录".equals(key)){
			key = "";
		}
		String bucket = lblNewLabel.getText().trim();
		if (key != null && !StringUtil.isEmptyString(bucket)) {
			newListAliyunFilesRunnable(bucket, key);
		}
	}
}
