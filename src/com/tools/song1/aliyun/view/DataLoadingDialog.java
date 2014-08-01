package com.tools.song1.aliyun.view;

import java.io.File;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.LayoutUtil;

public class DataLoadingDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DataLoadingDialog(Shell parent, int style) {
		super(parent, style);
		setText("确认文件删除");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		getParent().setEnabled(false);
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		LayoutUtil.centerShell(getParent(), shell);
		
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.NO_TRIM);
		shell.setAlpha(200);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.setImage(null);
		shell.setSize(158, 32);
		
		Composite composite = formToolkit.createComposite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite.setBounds(0, 0, 32, 32);
		formToolkit.paintBordersFor(composite);
		ImageViewer ic = new ImageViewer(composite);
		ImageLoader loader = new ImageLoader();
		File file = FileDoUtil.findFile("images/loading.gif");
		ImageData[] imageDatas = loader.load(file.getAbsolutePath());
		if (imageDatas.length == 0)
			return;
		else if (imageDatas.length == 1) {
			ic.setImage(imageDatas[0]);
		} else {
			ic.setImages(imageDatas, loader.repeatCount);
		}
		ic.pack();
		
		Label lblNewLabel = formToolkit.createLabel(shell, "正在加载.......", SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("思源黑体 CN Bold", 11, SWT.NORMAL));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel.setBounds(43, 5, 104, 20);
		

	}
	public void close(){
		getParent().setEnabled(true);
		shell.dispose();
	}
}
