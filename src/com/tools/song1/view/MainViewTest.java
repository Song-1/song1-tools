package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class MainViewTest {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtNewText;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainViewTest window = new MainViewTest();
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
		shell.setImage(SWTResourceManager.getImage(MainViewTest.class, "/images/song1.png"));
		shell.setSize(1000, 600);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("     菜单     ");
		
		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);
		
		MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem.setImage(SWTResourceManager.getImage(MainViewTest.class, "/images/tray.png"));
		mntmNewItem.setText("阿里云文件操作");
		
		MenuItem mntmNewItem_1 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_1.setText("樱桃时光");
		
		MenuItem mntmNewItem_2 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_2.setText("状元听书");
		
		MenuItem mntmNewItem_3 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_3.setText("享CD");
		
		MenuItem mntmNewItem_4 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_4.setText("退出");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.right = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		
		Label lblNewLabel = formToolkit.createLabel(composite, "New Label", SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 12, SWT.NORMAL));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		fd_lblNewLabel.top = new FormAttachment(0, 10);
		fd_lblNewLabel.right = new FormAttachment(0, 497);
		fd_lblNewLabel.bottom = new FormAttachment(0, 40);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		
		FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(lblNewLabel, 7,SWT.BOTTOM);
		fd_label.top = new FormAttachment(lblNewLabel, 5,SWT.BOTTOM);
		fd_label.right = new FormAttachment(100,0);
		fd_label.left = new FormAttachment(0);
		label.setLayoutData(fd_label);
		formToolkit.adapt(label, true, true);
		
		Composite composite_1 = formToolkit.createComposite(composite, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(100,-10);
		fd_composite_1.top = new FormAttachment(label, 5);
		fd_composite_1.left = new FormAttachment(0, 10);
		fd_composite_1.right = new FormAttachment(100,-10);
		composite_1.setLayoutData(fd_composite_1);
		formToolkit.paintBordersFor(composite_1);
		
		Label lblNewLabel_1 = formToolkit.createLabel(composite_1, "New Label", SWT.NONE);
		lblNewLabel_1.setBounds(10, 10, 143, 17);
		
		txtNewText = formToolkit.createText(composite_1, "New Text", SWT.NONE);
		txtNewText.setBounds(172, 10, 676, 23);
		
		TabFolder tabFolder = new TabFolder(composite_1, SWT.NONE);
		tabFolder.setBounds(10, 48, 944, 422);
		formToolkit.adapt(tabFolder);
		formToolkit.paintBordersFor(tabFolder);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setImage(SWTResourceManager.getImage(MainViewTest.class, "/images/folder.png"));
		tbtmNewItem.setText("New Item");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_2);
		formToolkit.paintBordersFor(composite_2);
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setImage(SWTResourceManager.getImage(MainViewTest.class, "/images/folder.png"));
		tbtmNewItem_1.setText("New Item");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_1.setControl(composite_3);
		formToolkit.paintBordersFor(composite_3);
		
		TabItem tbtmNewItem_2 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_2.setText("New Item");
		
		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_2.setControl(composite_4);
		formToolkit.paintBordersFor(composite_4);
		
		

	}
}
