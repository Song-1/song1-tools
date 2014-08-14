package com.tools.song1.view.aliyun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.aliyun.view.AliyunFileSelectDialog;
import com.tools.song1.aliyun.view.DataLoadingDialog;
import com.tools.song1.aliyun.view.ErrorDialog;
import com.tools.song1.aliyun.view.ModiyFileNameDialog;
import com.tools.song1.aliyun.view.NewFolderDialog;
import com.tools.song1.aliyun.view.UploadFilesDialog;
import com.tools.song1.runnable.aliyun.ListAliyunFilesRunnable;
import com.tools.song1.util.StringUtil;

public class AliyunMainComposite extends Composite {
	private Table bucketTable;
	private Composite composite_1;
	private Label bucketLable;
	private Text text;
	private Table aliyunFileViewTable;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AliyunMainComposite(Composite parent, int style) {
		super(parent, style);
		setSize(980, 380);
		setLayout(null);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(10, 0, 213, 380);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label.setBounds(212, 0, 2, 380);
		
		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(0, 38, 212, 2);
		
		TableViewer tableViewer = new TableViewer(composite,SWT.FULL_SELECTION);
		bucketTable = tableViewer.getTable();
		bucketTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem item = (TableItem)e.item;
				if(item != null){
					String bucket = (String)item.getData();
					updateBucketLable(bucket, bucket);
					newListAliyunFilesRunnable(bucket, "");
				}
			}
		});
		bucketTable.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		bucketTable.setBounds(0, 45, 213, 330);
		
		
		
		TableColumn tblclmnNewColumn = new TableColumn(bucketTable, SWT.NONE);
		tblclmnNewColumn.setWidth(212);
		tblclmnNewColumn.setText("操作选择");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel.setBounds(0, 10, 71, 25);
		lblNewLabel.setText("Bucket：");
		
		bucketLable = new Label(composite, SWT.NONE);
		bucketLable.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		bucketLable.setBounds(75, 10, 131, 25);
		
		composite_1 = new Composite(this, SWT.NONE);
		composite_1.setBounds(229, 0, 740, 380);
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setBounds(0, 0, 740, 40);
		
		Label label_2 = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBounds(0, 38, 740, 2);
		
		ToolBar toolBar = new ToolBar(composite_2, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(5, 0, 730, 40);
		
		ToolItem tltmNewItem = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String key = (String)text.getData();
				if("".equals(key)){
					new ErrorDialog(getParent().getShell(), SWT.NONE).open("当前目录已经是根目录");
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
					String bucket = (String)bucketLable.getData();
					if (key != null && !StringUtil.isEmptyString(bucket)) {
						newListAliyunFilesRunnable(bucket, key);
					}
				}
			}
		});
		tltmNewItem.setWidth(2);
		tltmNewItem.setImage(SWTResourceManager.getImage(AliyunMainComposite.class, "/images/up.png"));
		tltmNewItem.setToolTipText("返回上级");
		
		ToolItem tltmNewItem_1 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		tltmNewItem_1.setToolTipText("刷新");
		tltmNewItem_1.setImage(SWTResourceManager.getImage(AliyunMainComposite.class, "/images/refresh_bucket.png"));
		
		ToolItem tltmNewItem_2 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String,Object> params = new HashMap<String, Object>();
				String bucket = (String) bucketLable.getData();
				String key = (String)text.getData();
				params.put("bucket", bucket);
				params.put("key", key);
				NewFolderDialog newFloder = new NewFolderDialog(getParent().getShell(),SWT.NONE);
				newFloder.setParams(params);
				newFloder.open();
				refresh();
			}
		});
		tltmNewItem_2.setImage(SWTResourceManager.getImage(AliyunMainComposite.class, "/images/new.png"));
		tltmNewItem_2.setToolTipText("新建文件夹");
		
		ToolItem tltmNewItem_3 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String,String> params = new HashMap<String, String>();
				String bucket = (String) bucketLable.getData();
				String key = (String)text.getData();
				if(StringUtil.isEmptyString(bucket)){
					new ErrorDialog(getParent().getShell(), SWT.NONE).open("阿里云BUCKET不能为空");
				}else if(key == null){
					new ErrorDialog(getParent().getShell(), SWT.NONE).open("当前目录不能为空");
				}else {
					params.put("bucket", bucket);
					params.put("key", key);
					UploadFilesDialog uploadFilesDialog = new UploadFilesDialog(getParent().getShell(), SWT.NONE);
					uploadFilesDialog.setParentParamMap(params);
					uploadFilesDialog.open();
				}
			}
		});
		tltmNewItem_3.setToolTipText("文件上传");
		tltmNewItem_3.setImage(SWTResourceManager.getImage(AliyunMainComposite.class, "/images/upload.png"));
		
		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setBounds(5, 45, 730, 332);
		
		Label lblNewLabel_2 = new Label(composite_3, SWT.NONE);
		lblNewLabel_2.setImage(SWTResourceManager.getImage(AliyunMainComposite.class, "/images/folder.png"));
		lblNewLabel_2.setBounds(0, 4, 20, 17);
		
		text = new Text(composite_3, SWT.READ_ONLY);
		text.setText("请问天气");
		text.setBounds(20, 5, 700, 20);
		
		aliyunFileViewTable = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		aliyunFileViewTable.setBounds(0, 28, 725, 300);
		aliyunFileViewTable.setHeaderVisible(true);
		aliyunFileViewTable.addMouseListener(new TableMouseAdapter());
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(aliyunFileViewTable, SWT.NONE);
		tblclmnNewColumn_1.setWidth(433);
		tblclmnNewColumn_1.setText("文件");
		
		TableItem tableItem = new TableItem(aliyunFileViewTable, SWT.NONE);
		tableItem.setText("New TableItem");
		
		TableColumn tblclmnNewColumn_2 = new TableColumn(aliyunFileViewTable, SWT.NONE);
		tblclmnNewColumn_2.setWidth(125);
		tblclmnNewColumn_2.setText("大小");
		
		TableColumn tblclmnNewColumn_3 = new TableColumn(aliyunFileViewTable, SWT.NONE);
		tblclmnNewColumn_3.setWidth(137);
		tblclmnNewColumn_3.setText("最后修改时间");
		init();

	}
	
	private void init() {
		List<String> buckets = OSSUploadUtil.listAllBucketName();
		// String selectBucket = null;
		bucketTable.clearAll();
		bucketTable.removeAll();
		if (buckets != null && !buckets.isEmpty()) {
			// selectBucket = buckets.get(0);
			int i = 0;
			for (String bucket : buckets) {
				if(StringUtil.isEmptyString(bucket)){
					continue;
				}
				TableItem tableItem = new TableItem(bucketTable, SWT.NONE);
				tableItem.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
				tableItem.setImage(SWTResourceManager.getImage(AliyunMainComposite.class, "/images/bucket4.png"));
				tableItem.setText(bucket);
				tableItem.setData(bucket);
				if(i==0){
					updateBucketLable(bucket, bucket);
					newListAliyunFilesRunnable(bucket, "");
					bucketTable.setSelection(0);
				}
				i++;
			}
			
		}
		bucketTable.update();
	}
	
	/**
	 * 更新选中bucket的显示文本和数据
	 * @param text
	 * @param data
	 */
	private void updateBucketLable(String text,String data){
		bucketLable.setText(text);
		bucketLable.setData(data);
		bucketLable.update();
	}
	
	/**
	 * 更新当前选中的KEY值
	 * @param textStr
	 * @param data
	 */
	private void updateCurrentKey(String textStr,String data){
		if("".equals(textStr)){
			textStr = "根目录";
		}
		text.setText(textStr);
		text.setData(data);
		text.update();
	}
	
	private void newListAliyunFilesRunnable(String bucket, String key) {
		updateCurrentKey(key, key);
		DataLoadingDialog d = new DataLoadingDialog(getParent().getShell(),SWT.NONE);
		ListAliyunFilesRunnable listAliyunFilesRunnable = new ListAliyunFilesRunnable(aliyunFileViewTable, bucket, key,d);
		new Thread(listAliyunFilesRunnable).start();
		d.open();
	}
	
	private void refresh(){
		String key = (String)text.getData();
		String bucket = (String)bucketLable.getData();
		if (key != null && !StringUtil.isEmptyString(bucket)) {
			newListAliyunFilesRunnable(bucket, key);
		}
	}
	/**
	 * 新建表格的右键菜单
	 * 
	 * @return
	 */
	private Menu getTableMenu(boolean isFloder,Table muneTable) {
		final Table table = muneTable;
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
						String bucket = (String)bucketLable.getData();
						if (key != null && !StringUtil.isEmptyString(bucket)) {
							ModiyFileNameDialog modiyFileNameDialog = new ModiyFileNameDialog(getParent().getShell(), SWT.NONE);
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
						String bucket = (String)bucketLable.getData();
						if (key != null && !StringUtil.isEmptyString(bucket)) {
							Map<String,String> params = new HashMap<String, String>();
							params.put("sourceBucketName", bucket);
							params.put("sourcekey", key);
							params.put("sourceName", tableItem.getText());
							params.put("doFlag", "copyTo");
							AliyunFileSelectDialog aliyunFileSelectDialog = new AliyunFileSelectDialog(getParent().getShell(), SWT.NONE,params);
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
						String bucket = (String)bucketLable.getData();
						if (key != null && !StringUtil.isEmptyString(bucket)) {
							Map<String,String> params = new HashMap<String, String>();
							params.put("sourceBucketName", bucket);
							params.put("sourcekey", key);
							params.put("sourceName", tableItem.getText());
							params.put("doFlag", "cmoveTo");
							AliyunFileSelectDialog aliyunFileSelectDialog = new AliyunFileSelectDialog(getParent().getShell(), SWT.NONE,params);
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
						String bucket = (String)bucketLable.getData();
						if (key != null && !StringUtil.isEmptyString(bucket)) {
							OSSUploadUtil.deleteKey(bucket, key);
							new ErrorDialog(getParent().getShell(), SWT.NONE).open("删除成功");
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
				TableItem[] items = aliyunFileViewTable.getSelection();
				if (items != null && items.length > 0) {
					TableItem tableItem = items[0];
					if (tableItem != null) {
						String key = (String) tableItem.getData();
						Menu menu = null;
						if (key == null || (!StringUtil.isEmptyString(key) && !key.endsWith("/"))) {
							menu = getTableMenu(false,aliyunFileViewTable);
						} else {
							menu = getTableMenu(true,aliyunFileViewTable);
						}
						aliyunFileViewTable.setMenu(menu);
					}
				}
			}
		}
	}
	
	private void tableSelectToDo() {
		TableItem[] items = aliyunFileViewTable.getSelection();
		if (items != null && items.length > 0) {
			TableItem tableItem = items[0];
			if (tableItem != null) {
				String key = (String) tableItem.getData();
				if (key == null || (!StringUtil.isEmptyString(key) && !key.endsWith("/"))) {
					return;
				}
				String bucket = (String)bucketLable.getData();
				if (key != null && !StringUtil.isEmptyString(bucket)) {
					System.out.println(key);
					newListAliyunFilesRunnable(bucket, key);
				}
			}
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
