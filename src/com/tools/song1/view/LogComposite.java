package com.tools.song1.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.util.StringUtil;

public class LogComposite extends Composite {
	public static List<String> LOG_LIST = new ArrayList<String>();
	public static int LOG_LIST_INDEX = 0;
	private Text text;
	private Text text_1;
	
	public void outLog(){
		if(LOG_LIST != null && LOG_LIST_INDEX > 0){
			String log = text_1.getText();
			StringBuilder strb = new StringBuilder();
			strb.append(log);
			for(int i = LOG_LIST_INDEX ,size = LOG_LIST.size();i < size; i++){
				if(LOG_LIST_INDEX > 1000){
					strb = new StringBuilder();
				}
				strb.append("\\r\\n" + (String)LOG_LIST.get(i));
				LOG_LIST_INDEX++;
			}
			text_1.setText(strb.toString());
		}
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LogComposite(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setSize(800,600);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.BOLD));
		lblNewLabel.setBounds(10, 15, 113, 30);
		lblNewLabel.setText("操作日志");
		
		text = new Text(this, SWT.NONE);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text.setEditable(false);
		text.setBounds(10, 45, 780, 18);
		text.setText("sss sdufgsdg sghsfdgh  ");
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 67, 780, 2);
		
		text_1 = new Text(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text_1.setBounds(10, 67, 780, 513);
//		text_1.addListener(SWT.CHANGED, new Listener() {
//			@Override
//			public void handleEvent(Event arg0) {
//				String str = text_1.getText();
//				if(!StringUtil.isEmptyString(str)){
//					System.out.println(str);
//				}
//			}
//		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
