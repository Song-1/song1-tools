package com.tools.song1.view.song1;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.view.aliyun.AliyunMainComposite;
import com.tools.song1.view.song1.song.SongMainComposite;

public class MainComposite extends Composite {
	private Text text;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MainComposite(Composite parent, int style,String type) {
		super(parent, style);
		setSize(990, 550);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(4, 2, 980, 380);
		
		if("song1".equalsIgnoreCase(type)){
			new SongMainComposite(composite, SWT.NONE);
		}else if("aliyun".equalsIgnoreCase(type)){
			new AliyunMainComposite(composite, SWT.NONE);
		}else{
			new SongMainComposite(composite, SWT.NONE);
		}
		text = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		text.setBounds(4, 389, 980, 150);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
