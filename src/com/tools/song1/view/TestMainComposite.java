/**
 * 
 */
package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class TestMainComposite extends Composite {

	protected Composite shell;
	protected Shell shcld;
	private Tree tree;
	private Label lblNewLabel;
	private Composite composite_1;
	private Composite composite;
	
	public TestMainComposite(Composite parent, int style) {
		super(parent, style);
		shell = this;
		shcld = getParent().getShell();
		//shell.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/song1.png"));
		shell.setSize(990, 550);
		shell.setLayout(null);

		composite = new Composite(shell, SWT.NONE);
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
					}
				}
			}
		});
		tree.setBounds(0, 0, 187, 500);
		
		TreeItem trtmNewTreeitem_1 = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem_1.setText("New TreeItem");
		
		TreeItem trtmNewTreeitem_2 = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem_2.setText("New TreeItem");
		
		TreeItem trtmNewTreeitem_3 = new TreeItem(trtmNewTreeitem_2, SWT.NONE);
		trtmNewTreeitem_3.setText("New TreeItem");
		trtmNewTreeitem_2.setExpanded(true);
		
//		TreeItem trtmNewTreeitem_1 = new TreeItem(tree, SWT.NONE);
//		trtmNewTreeitem_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
//		trtmNewTreeitem_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 10, SWT.NORMAL));
//		trtmNewTreeitem_1.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/bucket4.png"));
//		trtmNewTreeitem_1.setText("New TreeItem");
		
		composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(210, 51, 775, 499);
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
		lblNewLabel_1.setText("功能选项：");
		tree.forceFocus();
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}