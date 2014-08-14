package com.tools.song1.view.song1;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.util.JavascriptUtil;
import com.tools.song1.util.LayoutUtil;
import com.tools.song1.view.aliyun.AliyunMainComposite;
import com.tools.song1.view.song1.song.SongMainComposite;
import com.upload.aliyun.MusicConstants;

public class Song1Main {

	protected Shell shell;
	private Text text;
	private AliyunMainComposite aliyunMainComposite;
	private SongMainComposite songMainComposite;
	private StackLayout compositeStackLayout;
	private Composite composite1;

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
			Song1Main window = new Song1Main();
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
		LayoutUtil.centerShell(display, shell);
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
		shell = new Shell( SWT.MIN|SWT.CLOSE);
		shell.setSize(1000, 600);
		shell.setText("音乐1号小工具");
		shell.setImage(SWTResourceManager.getImage(Song1Main.class, "/images/song1.png"));
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		new MenuItem(menu, SWT.SEPARATOR);
		
		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("   菜单   ");
		
		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);
		
		MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				aliyunMainComposite = new AliyunMainComposite(composite1, SWT.NONE);
				compositeStackLayout.topControl = aliyunMainComposite;
				aliyunMainComposite.setFocus();
				composite1.layout();
				shell.setText("阿里云小工具");
				shell.setImages(new Image[]{SWTResourceManager.getImage(Song1Main.class, "/images/tray.png"),SWTResourceManager.getImage(Song1Main.class, "/images/aliyun_icon.png")});
				//shell.setImage(SWTResourceManager.getImage(Song1Main.class, "/images/aliyun_icon.png"));
				shell.update();
			}
		});
		mntmNewItem.setImage(SWTResourceManager.getImage(Song1Main.class, "/images/tray.png"));
		mntmNewItem.setText("阿里云小工具");
		
		MenuItem mntmNewItem_1 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				compositeStackLayout.topControl = songMainComposite;
				songMainComposite.setFocus();
				composite1.layout();
				shell.setText("音乐1号小工具");
				shell.setImage(SWTResourceManager.getImage(Song1Main.class, "/images/song1.png"));
				shell.update();
			}
		});
		mntmNewItem_1.setImage(SWTResourceManager.getImage(Song1Main.class, "/images/Audio CD.png"));
		mntmNewItem_1.setText("音乐1号小工具");
		
		MenuItem mntmNewItem_2 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
				System.exit(0);
			}
		});
		mntmNewItem_2.setText("退出");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(2, 2, 992, 552);
		//new MainComposite(composite, SWT.NONE,"song1");
		composite1 = new Composite(composite, SWT.NONE);
		composite1.setBounds(4, 2, 980, 380);
		compositeStackLayout = new StackLayout();
		composite1.setLayout(compositeStackLayout);
		songMainComposite = new SongMainComposite(composite1, SWT.NONE);
		
//		
//		childComposite = new SongMainComposite(composite1, SWT.NONE);
//		childComposite.setFocus();
		
		text = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		text.setBounds(4, 389, 980, 150);
	}
}
