package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.util.JavascriptUtil;
import com.tools.song1.view.aliyun.AliyunMainComposite;
import com.upload.aliyun.MusicConstants;

public class MainViewTest {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Composite composite_1;
	private Label lblNewLabel;

	/**
	 * Launch the application.
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
		shell.setSize(1000, 650);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("     菜单     ");
		
		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);
		
		MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblNewLabel.setText("阿里云文件操作");
				lblNewLabel.update();
				AliyunMainComposite aliyun = new AliyunMainComposite(composite_1, SWT.NONE);
				FormData fd_composite_1 = new FormData();
				fd_composite_1.bottom = new FormAttachment(100,0);
				fd_composite_1.top = new FormAttachment(0, 0);
				fd_composite_1.left = new FormAttachment(0, 0);
				fd_composite_1.right = new FormAttachment(100, 0);
				aliyun.setLayoutData(fd_composite_1);
				formToolkit.paintBordersFor(aliyun);
			}
		});
		mntmNewItem.setImage(SWTResourceManager.getImage(MainViewTest.class, "/images/tray.png"));
		mntmNewItem.setText("阿里云文件操作");
		
		MenuItem mntmNewItem_1 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				TestMainComposite test = new TestMainComposite(composite_1, SWT.NONE);
//				test.setl
				MainComposite main = new MainComposite(composite_1, SWT.NONE);
				main.setLayout(new FormLayout());
			}
		});
		mntmNewItem_1.setText("音乐1号");
		
		MenuItem mntmNewItem_4 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display d = shell.getDisplay();
				shell.dispose();
				d.dispose();
			}
		});
		mntmNewItem_4.setText("退出");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.right = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		
		lblNewLabel = formToolkit.createLabel(composite, "New Label", SWT.NONE);
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
		
		composite_1 = formToolkit.createComposite(composite, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite_1.setLayout(null);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(100,0);
		fd_composite_1.top = new FormAttachment(label, 5);
		fd_composite_1.left = new FormAttachment(0, 0);
		fd_composite_1.right = new FormAttachment(100, 0);
		composite_1.setLayoutData(fd_composite_1);
		formToolkit.paintBordersFor(composite_1);
		

	}
}
