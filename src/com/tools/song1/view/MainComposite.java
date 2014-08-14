package com.tools.song1.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;

public class MainComposite extends Composite {
	private Text text;
	
	public MainComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		Button btnNewButton = new Button(this, SWT.NONE);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(0, 79);
		fd_btnNewButton.left = new FormAttachment(0, 143);
		fd_btnNewButton.bottom = new FormAttachment(0, 113);
		fd_btnNewButton.right = new FormAttachment(0, 264);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("New Button");
		
		text = new Text(this, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(0, 375);
		fd_text.top = new FormAttachment(btnNewButton, 46);
		fd_text.left = new FormAttachment(0, 160);
		text.setLayoutData(fd_text);
	}



	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
