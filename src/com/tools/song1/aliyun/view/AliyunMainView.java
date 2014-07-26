package com.tools.song1.aliyun.view;

import java.io.File;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.runnable.aliyun.ListAliyunFilesRunnable;
import com.upload.aliyun.MusicConstants;
import com.upload.aliyun.util.FileDoUtil;
import com.upload.aliyun.util.JavascriptUtil;
import com.upload.aliyun.util.StringUtil;

public class AliyunMainView {

	protected Shell shell;
	private Table table;
	private Tree tree;
	private Label lblNewLabel;
	private Composite composite_1;
	private Composite composite_2;

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
		shell.setText("SWT Application");
		shell.setLayout(null);

		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setBounds(10, 40, 191, 510);

		tree = new Tree(composite, SWT.NONE);
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

		composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(210, 67, 772, 362);
		composite_2 = new Composite(composite_1, SWT.NONE);

		composite_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		composite_2.setLocation(300, 120);
		composite_2.setSize(32, 32);
		composite_2.setVisible(false);
		Label label_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(210, 64, 774, 2);

		Label label = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label.setBounds(207, 0, 4, 570);

		lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel.setBounds(220, 22, 244, 35);

		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel_1.setBounds(10, 10, 191, 25);
		lblNewLabel_1.setText("阿里云BUCKET：");

		table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.addMouseListener(new TableMouseAdapter());

		table.setBounds(0, 0, 772, 348);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
		tableColumn.setWidth(666);
		tableColumn.setText("文件名称");
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
		tableItem.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/folder.png"));
		tableItem.setText("  sgsdfg  ");
		// Menu menu = new Menu(table);
		//
		// MenuItem mntmTest = new MenuItem(menu, SWT.NONE);
		// mntmTest.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// }
		// });
		// mntmTest.setText("test");
		//
		// MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		// mntmNewItem.setText("New Item");
		// table.setMenu(menu);
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
				trtmNewTreeitem.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/bucket4.png"));
				System.out.println(bucket.length());
				if (bucket.length() < namesize) {
					for (int i = bucket.length(); i < namesize; i++)
						bucket += " ";
				}
				System.out.println(bucket.length());
				trtmNewTreeitem.setText(bucket);
			}
		}
	}

	private void loadingImage() {
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
							new ModiyFileNameDialog(shell, SWT.NONE,bucket,key).open();
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
				System.out.println("............. test.........");
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
						// String bucket = lblNewLabel.getText();
						// if (key != null && !StringUtil.isEmptyString(bucket))
						// {
						// newListAliyunFilesRunnable(bucket, key);
						// }
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
		ListAliyunFilesRunnable listAliyunFilesRunnable = new ListAliyunFilesRunnable(table, bucket, key);
		Display.getCurrent().syncExec(listAliyunFilesRunnable);
	}
}
