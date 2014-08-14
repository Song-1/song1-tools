package com.tools.song1.view.song1.song;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class SongMainComposite extends Composite {
	private Table table;
	private Composite composite_1;
	private StackLayout compositeStackLayout;
	private MusicComposite musicComposite ;
	private EnjoyCDComposite enjoyCDComposite;
	private BookComposite bookComposite;
	private Label lblNewLabel_1;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SongMainComposite(Composite parent, int style) {
		super(parent, style);
		setSize(980, 380);
		setLayout(null);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(10, 0, 213, 380);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label.setBounds(212, 0, 2, 380);
		
		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(0, 35, 212, 2);
		
		TableViewer tableViewer = new TableViewer(composite,SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem item = (TableItem)e.item;
				if(item != null){
					SongMenuType type = (SongMenuType)item.getData();
					lblNewLabel_1.setText(type.getText());
					lblNewLabel_1.update();
					switch (type) {
					case SONG:
						compositeStackLayout.topControl = musicComposite; break;
					case BOOK:
						compositeStackLayout.topControl = bookComposite; break;
					case ENJOYCD:
						compositeStackLayout.topControl =enjoyCDComposite ; break;
					default:break;
					}
					composite_1.layout();
				}
			}
		});
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		table.setBounds(0, 40, 213, 330);
		
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setImage(SWTResourceManager.getImage(SongMainComposite.class, "/icons/Favorite.png"));
		tableItem.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		tableItem.setText(SongMenuType.SONG.getText());
		tableItem.setData(SongMenuType.SONG);
		
		TableItem tableItem_1 = new TableItem(table, SWT.NONE);
		tableItem_1.setImage(SWTResourceManager.getImage(SongMainComposite.class, "/icons/Document.png"));
		tableItem_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		tableItem_1.setText(SongMenuType.BOOK.getText());
		tableItem_1.setData(SongMenuType.BOOK);
		
		TableItem tableItem_2 = new TableItem(table, SWT.NONE);
		tableItem_2.setImage(SWTResourceManager.getImage(SongMainComposite.class, "/icons/CD.png"));
		tableItem_2.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		tableItem_2.setText(SongMenuType.ENJOYCD.getText());
		tableItem_2.setData(SongMenuType.ENJOYCD);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(212);
		tblclmnNewColumn.setText("操作选择");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel.setBounds(0, 10, 75, 25);
		lblNewLabel.setText("功能选择：");
		
		lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		lblNewLabel_1.setBounds(80, 10, 125, 25);
		
		composite_1 = new Composite(this, SWT.NONE);
		composite_1.setBounds(0, 0, 730, 370);
		composite_1.setBounds(229, 0, 740, 380);
		compositeStackLayout = new StackLayout();
		composite_1.setLayout(compositeStackLayout);
		musicComposite = new MusicComposite(composite_1, SWT.NONE);
		bookComposite = new BookComposite(composite_1, SWT.NONE);
		enjoyCDComposite = new EnjoyCDComposite(composite_1, SWT.NONE);
		
		table.setSelection(tableItem);
		table.setFocus();
		lblNewLabel_1.setText(tableItem.getText());
		lblNewLabel_1.update();

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
