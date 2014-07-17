package com.tools.song1.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

public class TestComposite extends Composite {
	private Text text;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TestComposite(Composite parent, int style) {
		super(parent, style);
		
		Group grpTestComposite = new Group(this, SWT.NONE);
		grpTestComposite.setText("   test composite    ");
		grpTestComposite.setBounds(82, 41, 248, 84);
		
		text = new Text(grpTestComposite, SWT.BORDER);
		text.setBounds(35, 30, 189, 23);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
