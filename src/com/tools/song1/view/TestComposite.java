package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class TestComposite extends Composite {
	private Table table;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TestComposite(Composite parent, int style) {
		super(parent, style);
		setSize(1000, 600);
		setLayout(new FormLayout());
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, 0);
		fd_composite.top = new FormAttachment(0, 0);
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.right = new FormAttachment(0, 250);
		composite.setLayoutData(fd_composite);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 0);
		fd_label.right = new FormAttachment(100, 0);
		fd_label.bottom = new FormAttachment(100,0);
		fd_label.left = new FormAttachment(0, 238);
		label.setLayoutData(fd_label);
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayout(new FormLayout());
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(0, 50);
		fd_composite_1.right = new FormAttachment(100,0);
		fd_composite_1.top = new FormAttachment(0);
		fd_composite_1.left = new FormAttachment(composite, 0);
		composite_1.setLayoutData(fd_composite_1);
		
		Label label_1 = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label_1 = new FormData();
		fd_label_1.bottom = new FormAttachment(100,0);
		fd_label_1.right = new FormAttachment(100,0);
		fd_label_1.top = new FormAttachment(0, 48);
		fd_label_1.left = new FormAttachment(0);
		label_1.setLayoutData(fd_label_1);
		
		Composite composite_2 = new Composite(this, SWT.BORDER);
		FormData fd_composite_2 = new FormData();
		fd_composite_2.bottom = new FormAttachment(100,-10);
		fd_composite_2.top = new FormAttachment(composite_1, 5);
		fd_composite_2.right = new FormAttachment(100,-10);
		fd_composite_2.left = new FormAttachment(composite, 5);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.left = new FormAttachment(0,10);
		fd_lblNewLabel.top = new FormAttachment(0, 10);
		fd_lblNewLabel.right = new FormAttachment(100,-10);
		fd_lblNewLabel.bottom = new FormAttachment(0,40);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("功能选项：");
		
		table = new Table(composite, SWT.NONE);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(lblNewLabel, 229, SWT.BOTTOM);
		fd_table.left = new FormAttachment(0,2);
		fd_table.top = new FormAttachment(lblNewLabel, 5);
		fd_table.right = new FormAttachment(100,-2);
		table.setLayoutData(fd_table);
		
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setImage(SWTResourceManager.getImage(MainComposite.class, "/images/cd.jpg"));
		tableItem.setText("New TableItem");
		
		TableItem tableItem_1 = new TableItem(table, SWT.NONE);
		tableItem_1.setText("New TableItem");
		
		TableItem tableItem_2 = new TableItem(table, SWT.NONE);
		tableItem_2.setText("New TableItem");
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(232);
		tblclmnNewColumn.setText("New Column");
		composite_2.setLayoutData(fd_composite_2);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
