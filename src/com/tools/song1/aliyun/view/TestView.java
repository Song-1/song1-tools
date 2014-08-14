package com.tools.song1.aliyun.view;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class TestView {

	protected Shell shell;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TestView window = new TestView();
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
	private Tree tree ;

	protected void createContents() {
		shell = new Shell();
		shell.setSize(1000, 600);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());

		composite = new Composite(shell, SWT.BORDER);
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(shell, 552, SWT.BOTTOM);
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
		fd_tree.bottom = new FormAttachment(composite, 537 , SWT.BOTTOM);
		fd_tree.right = new FormAttachment(lblNewLabel, 0, SWT.RIGHT);
		fd_tree.top = new FormAttachment(lblNewLabel,10, SWT.BOTTOM);
		fd_tree.left = new FormAttachment(-22);
		tree.setLayoutData(fd_tree);
		formToolkit.adapt(tree);
		formToolkit.paintBordersFor(tree);
		
		trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem.setImage(SWTResourceManager.getImage(TestView.class, "/images/bucket4.png"));
		trtmNewTreeitem.setText("New TreeItem");

		label = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		FormData fd_label = new FormData();
		fd_label.left = new FormAttachment(composite, 5);
		fd_label.top = new FormAttachment(0);
		fd_label.right = new FormAttachment(composite, 7, SWT.RIGHT);
		fd_label.bottom = new FormAttachment(composite, 48, SWT.BOTTOM);
		label.setLayoutData(fd_label);

		composite_1 = new Composite(shell, SWT.BORDER);
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
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_2 = new FormData();
		fd_composite_2.bottom = new FormAttachment(100,-10);
		fd_composite_2.top = new FormAttachment(label_1, 9);
		fd_composite_2.right = new FormAttachment(label_1, -10, SWT.RIGHT);
		fd_composite_2.left = new FormAttachment(0, 10);
		composite_2.setLayoutData(fd_composite_2);
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmNewItem = new MenuItem(menu, SWT.CASCADE);
		mntmNewItem.setText("dfgdfghd");
		mntmNewItem.setImage(SWTResourceManager.getImage(TestView.class, "/images/new_small.png"));
		
		Menu menu_1 = new Menu(mntmNewItem);
		mntmNewItem.setMenu(menu_1);
		
		
		
		shell.addControlListener(new ShellControlAdapter());
	}

	private class ShellControlAdapter extends ControlAdapter {
		@Override
		public void controlResized(ControlEvent e) {
			Point point = shell.getSize();
			int x = point.x;
			int y = point.y;
			System.out.println(x + " x " + y);
			System.out.println(y - 48);
			FormData fd_composite = new FormData();
			fd_composite.bottom = new FormAttachment(shell, y-48, SWT.BOTTOM);
			fd_composite.top = new FormAttachment(0, 10);
			fd_composite.left = new FormAttachment(0, 10);
			fd_composite.right = new FormAttachment(0, 180);
			composite.setLayoutData(fd_composite);
			FormData fd_tree = new FormData();
			fd_tree.bottom = new FormAttachment(composite, y-48 - 15 , SWT.BOTTOM);
			fd_tree.right = new FormAttachment(lblNewLabel, 0, SWT.RIGHT);
			fd_tree.top = new FormAttachment(lblNewLabel,10, SWT.BOTTOM);
			fd_tree.left = new FormAttachment(-22);
			tree.setLayoutData(fd_tree);
//			FormData fd_label = new FormData();
//			fd_label.left = new FormAttachment(composite, 5);
//			fd_label.top = new FormAttachment(0);
//			fd_label.right = new FormAttachment(composite, 7, SWT.RIGHT);
//			fd_label.bottom = new FormAttachment(0, (y*562)/600);
//			label.setLayoutData(fd_label);
//			FormData fd_composite_1 = new FormData();
//			fd_composite_1.bottom = new FormAttachment(label, -10, SWT.BOTTOM);
//			fd_composite_1.left = new FormAttachment(label, 5);
//			fd_composite_1.top = new FormAttachment(0, 10);
//			fd_composite_1.right = new FormAttachment(100, -10);
//			composite_1.setLayoutData(fd_composite_1);
			//shell.layout();
		}
	}
}
