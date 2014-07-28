package com.tools.song1.aliyun.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.util.LayoutUtil;
import com.upload.aliyun.util.StringUtil;

public class AliyunFileSelectDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Combo combo;
	private Tree  tree;
	private Map<String,String> parentParams = new HashMap<String, String>();

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AliyunFileSelectDialog(Shell parent, int style,Map<String,String> parentParams) {
		super(parent, style);
		setText("文件夹选择");
		this.parentParams = parentParams;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		LayoutUtil.centerShell(display, shell);
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
		shell.setSize(450, 445);
		shell.setText(getText());
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 414, 352);
		
		combo = new Combo(composite, SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tree.clearAll(true);
				tree.removeAll();
				Display.getCurrent().syncExec(new ListAliyunFileRunable(tree,null,""));
			}
		});
		combo.setBounds(97, 2, 307, 25);
		
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel.setBounds(10, 3, 81, 24);
		lblNewLabel.setText("BUCKET：");
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 33, 394, 2);
		
		tree = new Tree(composite, SWT.BORDER);
		tree.addTreeListener(new TreeAdapter() {
			@Override
			public void treeExpanded(TreeEvent e) {
				TreeItem item = (TreeItem) e.item;
				if(item != null){
					String key = (String)item.getData();
					System.out.println(key);
					Display.getCurrent().syncExec(new ListAliyunFileRunable(tree,item,key));
				}
			}
		});
		tree.setBounds(10, 68, 394, 284);
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 11, SWT.NORMAL));
		lblNewLabel_1.setBounds(10, 40, 216, 22);
		lblNewLabel_1.setText("请选择文件夹：");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnNewButton.setBounds(96, 368, 80, 27);
		btnNewButton.setText("取  消");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem items[] = tree.getSelection();
				if(items != null && items.length > 0){
					TreeItem item = items[0];
					if(item != null){
						String key = (String)item.getData();
						String bucket = combo.getText().trim();
						String sourceBucketName = parentParams.get("sourceBucketName");
						String sourceKey = parentParams.get("sourcekey");
						String sourceName = parentParams.get("sourceName");
						String doFlag = parentParams.get("doFlag");
						if(sourceKey.endsWith("/")){
							key += sourceName+"/";
						}else{
							int i = sourceKey.lastIndexOf(sourceName);
							String stuffix = "";
							if(i > 0){
								stuffix = new String(sourceKey.substring(i+sourceName.length()));
							}
							key += sourceName+stuffix;
						}
						boolean flag = OSSUploadUtil.modifyAliyunFloderName(sourceBucketName, sourceKey, bucket, key);
						if("copyTo".equalsIgnoreCase(doFlag)){
							if(flag){
								new ErrorDialog(shell, SWT.NONE).open("复制成功");
							}else{
								new ErrorDialog(shell, SWT.NONE).open("复制失败");
							}
						}else if("moveTo".equalsIgnoreCase(doFlag)){
							if(flag){
								OSSUploadUtil.deleteKey(sourceBucketName, sourceKey);
								new ErrorDialog(shell, SWT.NONE).open("移动成功");
							}else{
								new ErrorDialog(shell, SWT.NONE).open("移动失败");
							}
						}
					}
				}
				shell.dispose();
			}
		});
		btnNewButton_1.setBounds(267, 368, 80, 27);
		btnNewButton_1.setText("确  定");
		
		init();
	}
	
	private void init() {
		List<String> buckets = OSSUploadUtil.listAllBucketName();
		if (buckets != null && !buckets.isEmpty()) {
			String strs[] = new String[buckets.size()];
			buckets.toArray(strs);
			combo.setItems(strs);
			String sourceBucketName = parentParams.get("sourceBucketName");
			System.out.println("sourceBucketName ===== "+sourceBucketName);
			if(!StringUtil.isEmptyString(sourceBucketName) && buckets.contains(sourceBucketName.trim())){
				combo.setText(sourceBucketName.trim());
			}else{
				combo.setText(strs[0]);
			}
			combo.update();
		}
		
		Display.getCurrent().syncExec(new ListAliyunFileRunable(tree,null,""));
	}
	
	private class ListAliyunFileRunable implements Runnable{
		private String key ;
		private Tree tree;
		private TreeItem item;
		public ListAliyunFileRunable(Tree tree ,TreeItem item,String key){
			this.tree = tree;
			this.key = key;
			this.item = item;
		}

		@Override
		public void run() {
			String bucket = combo.getText().trim();
			if(item != null){
				TreeItem[] items = item.getItems();
				if(items != null){
					for (TreeItem treeItem : items) {
						listFloders(bucket, (String)treeItem.getData(), treeItem);
					}
				}
			}else{
				List<String> list = OSSUploadUtil.listAliyunFloder(bucket, key, false);
				if(list != null){
					for (String floderName : list) {
						TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
						trtmNewTreeitem.setImage(SWTResourceManager.getImage(AliyunFileSelectDialog.class, "/images/folder.png"));
						trtmNewTreeitem.setText(subStr(floderName));
						trtmNewTreeitem.setData(floderName);
						trtmNewTreeitem.setExpanded(false);
						listFloders(bucket,floderName,trtmNewTreeitem);
					}
				}
			}
			tree.update();
		}
		
		private void listFloders(String bucket,String key,TreeItem item){
			List<String> list = OSSUploadUtil.listAliyunFloder(bucket, key, false);
			if(list != null){
				for (String floderName : list) {
					TreeItem trtmNewTreeitem = null;
					if(item != null){
						trtmNewTreeitem = new TreeItem(item, SWT.NONE);
					}else{
						trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
					}
					trtmNewTreeitem.setImage(SWTResourceManager.getImage(AliyunFileSelectDialog.class, "/images/folder.png"));
					trtmNewTreeitem.setText(subStr(floderName));
					trtmNewTreeitem.setData(floderName);
					trtmNewTreeitem.setExpanded(false);
					///listFloders(bucket,floderName,trtmNewTreeitem);
				}
			}
		}
		
		private String subStr(String string){
			if (string.endsWith("/")) {
				string = new String(string.substring(0, string.length() - 1));
			}
			int i = string.lastIndexOf("/");
			if (i > 0) {
				string = new String(string.substring(i + 1));
			}
			return string;
		}
		
	}
}
