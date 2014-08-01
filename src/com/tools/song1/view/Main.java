package com.tools.song1.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.util.JavascriptUtil;
import com.tools.song1.util.MyPrintStream;
import com.tools.song1.util.StringUtil;
import com.tools.song1.util.SystemPropertiesUtil;
import com.tools.song1.view.enjoy.EnjoyAllComposite;
import com.tools.song1.view.enjoy.EnjoyByAlbumComposite;
import com.tools.song1.view.enjoy.EnjoyByAlbumStyleComposite;
import com.upload.aliyun.MusicConstants;

public class Main {

	protected Shell shell;
	private Text text;
	private Label label;
	private ScrolledComposite scrolledComposite;
	private Tree tree;
	private Label lblNewLabel;
	private Map<String, Composite> MENU_MAPPING = new HashMap<String, Composite>();
	private String[] MENU_MESSAGES = { "音乐一号", "享CD", "享CD全部上传", "享CD按风格上传", "享CD按专辑上传", "全局设置" };
	private Composite composite;
	private Composite enjoyAllComposite;
	private Composite enjoyByAlbumStyleComposite;
	private Composite enjoyByAlbumComposite;
	private GlobalSettingComposite globalSettingComposite;
	private StackLayout compositeStackLayout;
	private TreeItem trtmNewTreeitem;
	private MenuItem mntmNewItem_2;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MusicConstants.loadConfig();
			SystemPropertiesUtil.init();
			OSSUploadUtil.init();
			JavascriptUtil.init();
			Main window = new Main();
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
		shell.addShellListener(new ShellListener() {
			@Override
			public void shellIconified(ShellEvent arg0) {
			}

			@Override
			public void shellDeiconified(ShellEvent arg0) {
			}

			@Override
			public void shellDeactivated(ShellEvent arg0) {
			}

			@Override
			public void shellClosed(ShellEvent arg0) {
				Control[] controls = shell.getChildren();
				if (controls != null) {
					for (Control control : controls) {
						control.dispose();
					}
				}
			}

			@Override
			public void shellActivated(ShellEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
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
		shell.setImage(SWTResourceManager.getImage(Main.class, "/images/song1.png"));
		shell.setSize(965, 644);
		shell.setText(MENU_MESSAGES[0]);
		shell.setLayout(new FormLayout());

		text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		FormData fd_text = new FormData();
		fd_text.bottom = new FormAttachment(0, 578);
		fd_text.right = new FormAttachment(0, 943);
		fd_text.top = new FormAttachment(0, 407);
		fd_text.left = new FormAttachment(0, 5);
		text.setLayoutData(fd_text);
		outPutStream();

		label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label = new FormData();
		fd_label.right = new FormAttachment(0, 943);
		fd_label.top = new FormAttachment(0, 399);
		fd_label.left = new FormAttachment(0);
		label.setLayoutData(fd_label);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		new MenuItem(menu, SWT.SEPARATOR);
		
		MenuItem mntmNewItem_1 = new MenuItem(menu, SWT.NONE);
		mntmNewItem_1.setText("菜单");
		Menu menu_1 = new Menu(mntmNewItem_1);
		mntmNewItem_1.setMenu(menu_1);
		
		mntmNewItem_2 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_2.setImage(SWTResourceManager.getImage(Main.class, "/images/tray.png"));
		mntmNewItem_2.setText("  阿里云文件处理器");
		
		new MenuItem(menu, SWT.SEPARATOR);

		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.setText("   设置   ");

		Label label_1 = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		FormData fd_label_1 = new FormData();
		fd_label_1.bottom = new FormAttachment(0, 399);
		fd_label_1.top = new FormAttachment(0, 10);
		fd_label_1.left = new FormAttachment(0, 149);
		label_1.setLayoutData(fd_label_1);

		scrolledComposite = new ScrolledComposite(shell, SWT.H_SCROLL);
		FormData fd_scrolledComposite = new FormData();
		fd_scrolledComposite.bottom = new FormAttachment(0, 383);
		fd_scrolledComposite.right = new FormAttachment(0, 143);
		fd_scrolledComposite.top = new FormAttachment(0, 10);
		fd_scrolledComposite.left = new FormAttachment(0);
		scrolledComposite.setLayoutData(fd_scrolledComposite);
		scrolledComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		tree = new Tree(scrolledComposite, SWT.BORDER);
		scrolledComposite.setContent(tree);
		scrolledComposite.setMinSize(tree.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		tree.addSelectionListener(new TreeSelectionAdapter(tree));

		trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem.setText(MENU_MESSAGES[5]);

		TreeItem trtmNewTreeitem_4 = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem_4.setText(MENU_MESSAGES[1]);

		TreeItem treeItem_1 = new TreeItem(trtmNewTreeitem_4, SWT.NONE);
		treeItem_1.setText(MENU_MESSAGES[2]);

		TreeItem treeItem_2 = new TreeItem(trtmNewTreeitem_4, SWT.NONE);
		treeItem_2.setText(MENU_MESSAGES[3]);

		TreeItem treeItem_3 = new TreeItem(trtmNewTreeitem_4, SWT.NONE);
		treeItem_3.setText(MENU_MESSAGES[4]);

		lblNewLabel = new Label(shell, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(0, 38);
		fd_lblNewLabel.right = new FormAttachment(0, 326);
		fd_lblNewLabel.top = new FormAttachment(0, 18);
		fd_lblNewLabel.left = new FormAttachment(0, 158);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
		lblNewLabel.setText("");

		Label label_2 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label_2 = new FormData();
		fd_label_2.right = new FormAttachment(0, 946);
		fd_label_2.top = new FormAttachment(0, 46);
		fd_label_2.left = new FormAttachment(0, 150);
		label_2.setLayoutData(fd_label_2);

		composite = new Composite(shell, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 383);
		fd_composite.right = new FormAttachment(0, 939);
		fd_composite.top = new FormAttachment(0, 50);
		fd_composite.left = new FormAttachment(0, 155);
		composite.setLayoutData(fd_composite);
		compositeStackLayout = new StackLayout();
		composite.setLayout(compositeStackLayout);
		enjoyAllComposite = new EnjoyAllComposite(composite, SWT.NONE);
		enjoyByAlbumStyleComposite = new EnjoyByAlbumStyleComposite(composite, SWT.NONE);
		enjoyByAlbumComposite = new EnjoyByAlbumComposite(composite, SWT.NONE);
		globalSettingComposite = new GlobalSettingComposite(composite, SWT.NONE);
		System.out.println("init .....................");
		init(trtmNewTreeitem_4);

	}

	public void init(TreeItem selectItem) {
		tree.setSelection(selectItem);
		setLblNewLabelText("享CD");
		compositeStackLayout.topControl = enjoyAllComposite;
		composite.layout();
	}

	private void outPutStream() {
		// ///输出重定向设置
		MyPrintStream mps = new MyPrintStream(System.out, text);
		System.setOut(mps);
		System.setErr(mps);
	}

	private void setLblNewLabelText(String value) {
		if (StringUtil.isEmptyString(value)) {
			return;
		}
		lblNewLabel.setText(value);
	}

	public void setMenuValue() {
		MENU_MAPPING.put(MENU_MESSAGES[1], enjoyAllComposite);
		MENU_MAPPING.put(MENU_MESSAGES[2], enjoyAllComposite);
		MENU_MAPPING.put(MENU_MESSAGES[3], enjoyByAlbumStyleComposite);
		MENU_MAPPING.put(MENU_MESSAGES[4], enjoyByAlbumComposite);
		MENU_MAPPING.put(MENU_MESSAGES[5], globalSettingComposite);
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private class TreeSelectionAdapter extends SelectionAdapter {
		private Tree tree;

		public TreeSelectionAdapter(Tree tree) {
			this.tree = tree;
		}

		public void widgetSelected(SelectionEvent e) {
			TreeItem item = tree.getSelection()[0];
			if (item != null) {
				String itemName = item.getText();
				setLblNewLabelText(itemName);
				if (MENU_MESSAGES[1].equals(itemName)) {
					compositeStackLayout.topControl = enjoyAllComposite;
				} else if (MENU_MESSAGES[2].equals(itemName)) {
					compositeStackLayout.topControl = enjoyAllComposite;
				} else if (MENU_MESSAGES[3].equals(itemName)) {
					compositeStackLayout.topControl = enjoyByAlbumStyleComposite;
				} else if (MENU_MESSAGES[4].equals(itemName)) {
					compositeStackLayout.topControl = enjoyByAlbumComposite;
				} else if (MENU_MESSAGES[5].equals(itemName)) {
					compositeStackLayout.topControl = globalSettingComposite;
				}
				composite.layout();
			}
		}
	}
}
