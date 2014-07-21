package com.tools.song1.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tools.song1.util.LayoutUtil;
import org.eclipse.swt.widgets.Label;

public class ErrorWindow {

	protected Shell shell;
	private Label text;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ErrorWindow window = new ErrorWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ErrorWindow() {
	}

	public ErrorWindow(Display display, String str) {
		// Shell[] shells = display.getShells();
		// if(shells != null){
		// for (Shell s : shells) {
		// //s.setVisible(false);
		// s.setEnabled(false);
		// }
		// }
		createContents();
		shell.open();
		text.setText(str);
		shell.layout();
		LayoutUtil.centerShell(display, shell);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	public ErrorWindow(Composite parent, String str) {
		Display display = parent.getDisplay();
		createContents();
		shell.setParent(parent);
		shell.open();
		text.setText(str);
		shell.layout();
		shell.addShellListener(new ShellListener() {
			
			@Override
			public void shellIconified(ShellEvent arg0) {
				System.out.println("icc.........");
				
			}
			
			@Override
			public void shellDeiconified(ShellEvent arg0) {
				System.out.println("Deiconified..........");
				
			}
			
			@Override
			public void shellDeactivated(ShellEvent arg0) {
				System.out.println("Deactivated..............");
				shell.getDisplay().asyncExec(new Runnable() {
			        public void run() {
			            shell.setActive();
			        }
			    });
			}
			
			@Override
			public void shellClosed(ShellEvent arg0) {
				System.out.println("closed........");
				
			}
			
			@Override
			public void shellActivated(ShellEvent arg0) {
				System.out.println("ac...........");
				
			}
		});
		LayoutUtil.centerShell(parent, shell);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
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
		shell.setLocation(500, 100);
		shell.setSize(309, 187);
		shell.setText("提示");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		Composite composite_1 = new Composite(shell, SWT.NONE);

		text = new Label(composite_1, SWT.CENTER | SWT.WRAP);
		text.setBounds(10, 25, 273, 49);

		Composite composite = new Composite(shell, SWT.NONE);

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Display display = shell.getDisplay();
				shell.dispose();
				// Shell[] shells = display.getShells();
				// if(shells != null){
				// for (Shell s : shells) {
				// //s.setVisible(false);
				// s.setEnabled(true);
				// }
				// }
			}
		});
		btnNewButton.setBounds(105, 10, 80, 27);
		btnNewButton.setText("确  定");
	}

}
