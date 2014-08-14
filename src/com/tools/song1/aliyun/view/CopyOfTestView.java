package com.tools.song1.aliyun.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CopyOfTestView {

	protected Shell shell;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CopyOfTestView window = new CopyOfTestView();
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

	protected Composite composite;
	protected Label label;
	protected Composite composite_1;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private TreeItem trtmNewTreeitem;
	private Label lblNewLabel;
	private Tree tree;
	private Label label_2;
	private Table table;
	private TableColumn tblclmnNewColumn;
	private TableColumn tblclmnNewColumn_1;
	private TableColumn tblclmnNewColumn_2;
	private TableItem tableItem;

	protected void createContents() {
		shell = new Shell();
		shell.setSize(1000, 600);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());

		composite = new Composite(shell, SWT.BORDER);
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, -10);
		fd_composite.top = new FormAttachment(0, 10);
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.right = new FormAttachment(0, 180);
		composite.setLayoutData(fd_composite);

		lblNewLabel = formToolkit.createLabel(composite, "阿里云BUCKETS：", SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(0, 36);
		fd_lblNewLabel.top = new FormAttachment(0, 10);
		fd_lblNewLabel.right = new FormAttachment(100);
		fd_lblNewLabel.left = new FormAttachment(0, 5);
		lblNewLabel.setLayoutData(fd_lblNewLabel);

		tree = new Tree(composite, SWT.NONE);
		FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(100,0);
		fd_tree.right = new FormAttachment(lblNewLabel, 0, SWT.RIGHT);
		fd_tree.top = new FormAttachment(lblNewLabel, 10, SWT.BOTTOM);
		fd_tree.left = new FormAttachment(-22);
		tree.setLayoutData(fd_tree);
		formToolkit.adapt(tree);
		formToolkit.paintBordersFor(tree);

		trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem.setImage(SWTResourceManager.getImage(CopyOfTestView.class, "/images/bucket4.png"));
		trtmNewTreeitem.setText("New TreeItem");

		label = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		FormData fd_label = new FormData();
		fd_label.left = new FormAttachment(composite, 5);
		fd_label.top = new FormAttachment(0);
		fd_label.right = new FormAttachment(composite, 7, SWT.RIGHT);
		fd_label.bottom = new FormAttachment(composite, 48, SWT.BOTTOM);
		label.setLayoutData(fd_label);

		composite_1 = new Composite(shell, SWT.BORDER);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite_1.setLayout(new FormLayout());
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(composite, 0, SWT.BOTTOM);
		fd_composite_1.left = new FormAttachment(label, 5);
		fd_composite_1.top = new FormAttachment(0, 10);
		fd_composite_1.right = new FormAttachment(100, -10);
		composite_1.setLayoutData(fd_composite_1);

		Label lblNewLabel_1 = formToolkit.createLabel(composite_1, "New Label", SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.right = new FormAttachment(0, 231);
		fd_lblNewLabel_1.top = new FormAttachment(0, 10);
		fd_lblNewLabel_1.left = new FormAttachment(0, 10);
		fd_lblNewLabel_1.bottom = new FormAttachment(0, 36);
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_1);

		Label label_1 = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label_1 = new FormData();
		fd_label_1.bottom = new FormAttachment(lblNewLabel_1, 8, SWT.BOTTOM);
		fd_label_1.top = new FormAttachment(lblNewLabel_1, 6);
		fd_label_1.right = new FormAttachment(100, 0);
		fd_label_1.left = new FormAttachment(0);
		label_1.setLayoutData(fd_label_1);
		formToolkit.adapt(label_1, true, true);
		
		Composite composite_2 = formToolkit.createComposite(composite_1, SWT.NONE);
		composite_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite_2.setLayout(new FormLayout());
		FormData fd_composite_2 = new FormData();
		fd_composite_2.bottom = new FormAttachment(100,-10);
		fd_composite_2.top = new FormAttachment(label_1, 5);
		fd_composite_2.left = new FormAttachment(0);
		fd_composite_2.right = new FormAttachment(100,0);
		composite_2.setLayoutData(fd_composite_2);
		formToolkit.paintBordersFor(composite_2);
		
		label_2 = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label_2 = new FormData();
		fd_label_2.bottom = new FormAttachment(0, 57);
		fd_label_2.right = new FormAttachment(100, 0);
		fd_label_2.top = new FormAttachment(0, 55);
		fd_label_2.left = new FormAttachment(0);
		label_2.setLayoutData(fd_label_2);
		formToolkit.adapt(label_2, true, true);
		
		Composite composite_3 = formToolkit.createComposite(composite_2, SWT.NONE);
		composite_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		FormData fd_composite_3 = new FormData();
		fd_composite_3.right = new FormAttachment(100,-10);
		fd_composite_3.bottom = new FormAttachment(label_2, -2);
		fd_composite_3.left = new FormAttachment(0);
		fd_composite_3.top = new FormAttachment(0);
		composite_3.setLayoutData(fd_composite_3);
		formToolkit.paintBordersFor(composite_3);
		
		Button btnNewButton = formToolkit.createButton(composite_3, "New Button", SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				load();
			}
		});
		btnNewButton.setBounds(126, 8, 100, 39);
		btnNewButton.setImage(SWTResourceManager.getImage(AliyunMainView.class, "/images/refresh_bucket.png"));
		formToolkit.adapt(btnNewButton, true, true);
		btnNewButton.setText("刷新");
		
		Composite composite_4 = formToolkit.createComposite(composite_2, SWT.NONE);
		composite_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite_4.setLayout(new FormLayout());
		FormData fd_composite_4 = new FormData();
		fd_composite_4.bottom = new FormAttachment(100,-10);
		fd_composite_4.top = new FormAttachment(label_2, 5);
		fd_composite_4.right = new FormAttachment(100,-10);
		fd_composite_4.left = new FormAttachment(0, 10);
		composite_4.setLayoutData(fd_composite_4);
		formToolkit.paintBordersFor(composite_4);
		
		table = formToolkit.createTable(composite_4, SWT.NONE);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(100,0);
		fd_table.top = new FormAttachment(0);
		fd_table.left = new FormAttachment(0);
		fd_table.right = new FormAttachment(100,0);
		table.setLayoutData(fd_table);
		formToolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(292);
		tblclmnNewColumn.setText("名称");
		
		tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(130);
		tblclmnNewColumn_1.setText("大小");
		
		tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText("类型");
		
		tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(0,"New TableItem");
		tableItem.setText(1,"New TableItem");
		tableItem.setText(2,"New TableItem");
		
	}

	
	private void load(){
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				final DataLoadingDialog d = new DataLoadingDialog(shell,SWT.NONE);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								d.close();
							}
						});
					}
				}).start();
				d.open();
				
			}
		});
		
	}
}
