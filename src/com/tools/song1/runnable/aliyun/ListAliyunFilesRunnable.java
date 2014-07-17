/**
 * 
 */
package com.tools.song1.runnable.aliyun;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.tools.song1.aliyun.view.AliyunMainView;
import com.tools.song1.runnable.BaseRunnable;
import com.tools.song1.service.AliyunService;
import com.tools.song1.service.impl.AliyunServiceImpl;
import com.upload.aliyun.MusicConstants;
import com.upload.aliyun.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class ListAliyunFilesRunnable extends BaseRunnable {
	private Table table;
	private String bucketName;
	private String key;
	
	
	public ListAliyunFilesRunnable(Table table,String bucketName, String key){
		this.table = table;
		this.bucketName = bucketName;
		this.key = key;
	}

	@Override
	public void doRun() {
		AliyunService service = new AliyunServiceImpl();
		List<String> list = new ArrayList<String>();
		List<OSSObjectSummary> fileList = new ArrayList<OSSObjectSummary>();
		service.listAliyunFiles(bucketName, key, list, fileList);
		table.clearAll();
		//table.deselectAll();
		table.removeAll();
		if(!StringUtil.isEmptyString(key)){
			if(key.endsWith("/")){
				key = new String(key.substring(0,key.length() - 1 ));
			}
			int i = key.lastIndexOf("/");
			if(i < 0){
				key = "";
			}else{
				key = new String(key.substring(0,i+1));
			}
			TableItem tableItem1 = new TableItem(table, SWT.NONE);
			tableItem1.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/folder.png"));
			tableItem1.setText("返回上级");
			tableItem1.setData(key);
		}
		for (String string : list) {
			if(StringUtil.isEmptyString(string)){
				continue;
			}
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setData(string);
			tableItem.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/folder.png"));
			if (string.endsWith("/")) {
				string = new String(string.substring(0, string.length() - 1));
			}
			int i = string.lastIndexOf("/");
			if (i > 0) {
				string = new String(string.substring(i + 1));
			}
			tableItem.setText(string);
		}
		for (OSSObjectSummary os : fileList) {
			String osName = os.getKey();
			if(StringUtil.isEmptyString(osName)){
				continue;
			}else if(osName.endsWith("/")){
				continue;
			}
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setData(osName);
			int i = osName.lastIndexOf("/");
			if (i > 0) {
				osName = new String(osName.substring(i + 1));
			}
			i = osName.lastIndexOf(".");
			String stuffix = "";
			if (i > 0) {
				stuffix = new String(osName.substring(i ));
			}
			tableItem.setImage(SWTResourceManager.getImage(AliyunMainView.class, MusicConstants.getIconByStuffix(stuffix)));
			tableItem.setText(osName);
		}
	}
	
}