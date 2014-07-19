package com.tools.song1.view.enjoy;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.constants.EnjoyConstans;
import com.tools.song1.util.SystemPropertiesUtil;

public class EnjoyMainComposite extends Composite {
	private Text text;
	private Map<String,Object> composite_1Map = new HashMap<String, Object>();

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EnjoyMainComposite(Composite parent, int style) {
		super(parent, style);
		setSize(800, 529);
		Group group = new Group(this, SWT.NONE);
		group.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		group.setText(" 服务器基路径设置 ");
		group.setBounds(10, 50, 780, 65);
		
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setBounds(12, 30, 50, 17);
		lblNewLabel_1.setText("URL：");
		
		text = new Text(group, SWT.BORDER);
		text.setBounds(68, 27, 702, 23);
		text.setText(SystemPropertiesUtil.getProperty(EnjoyConstans.SERVER_BASE_URL));
		composite_1Map.put("enjoy.server.base.url", text.getText());
		text.addListener(SWT.CHANGED, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				composite_1Map.put("enjoy.server.base.url", text.getText());
			}
		});
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBounds(10, 125, 780, 400);

		TabItem tbtmAll = new TabItem(tabFolder, SWT.NONE);
		tbtmAll.setText("	全部上传	");

		AllSvaeComposite composite_1 = new AllSvaeComposite(tabFolder, SWT.NONE);
		composite_1.setParentDataMap(composite_1Map);
		tbtmAll.setControl(composite_1);


		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" 按风格上传 ");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText(" 按专辑上传 ");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_2);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.BOLD));
		lblNewLabel.setBounds(10, 11, 141, 22);
		lblNewLabel.setText("享CD数据处理");
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		btnNewButton.setBounds(693, 10, 80, 27);
		btnNewButton.setText("返  回");
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 39, 780, 2);
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
