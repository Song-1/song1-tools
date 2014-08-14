package com.tools.song1.view.song1.song;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tools.song1.aliyun.view.AliyunFloderSelectDialog;

public class BookComposite extends Composite {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtNewText;
	private Text txtNewText_1;
	private Text txtsavesonglistURL;
	private Text text_2;
	private Text txtsavesongURL;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public BookComposite(Composite parent, int style) {
		super(parent, style);
		setSize(740,380);
		
		Group group = new Group(this, SWT.NONE);
		group.setText("   阿里云  设置   ");
		group.setBounds(10, 10, 720, 90);
		
		Label lblNewLabel = formToolkit.createLabel(group, "bucket：", SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel.setBounds(10, 25, 80, 17);
		
		txtNewText = formToolkit.createText(group, "New Text", SWT.READ_ONLY);
		txtNewText.setText("");
		txtNewText.setBounds(95, 22, 400, 23);
		
		Label lblNewLabel_1 = formToolkit.createLabel(group, "key：", SWT.NONE);
		lblNewLabel_1.setAlignment(SWT.RIGHT);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_1.setBounds(10, 60, 80, 17);
		
		txtNewText_1 = formToolkit.createText(group, "New Text", SWT.READ_ONLY);
		txtNewText_1.setText("");
		txtNewText_1.setBounds(95, 57, 400, 23);
		
		Button btnNewButton = formToolkit.createButton(group, "选  择", SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AliyunFloderSelectDialog d = new AliyunFloderSelectDialog(getShell(), SWT.NONE);
				Map<String,String> map = d.open();
				if(map != null){
					txtNewText.setText(map.get("bucket") != null ? map.get("bucket") : "");
					txtNewText_1.setText(map.get("key")!= null ? map.get("key") : "");
				}
				txtNewText_1.setFocus();
			}
		});
		btnNewButton.setBounds(505, 55, 80, 27);
		
		Group grpcd = new Group(this, SWT.NONE);
		grpcd.setText("   状元听书  设置   ");
		grpcd.setBounds(10, 114, 720, 160);
		
		Label lblNewLabel_2 = formToolkit.createLabel(grpcd, "书单URL：", SWT.NONE);
		lblNewLabel_2.setAlignment(SWT.RIGHT);
		lblNewLabel_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_2.setBounds(10, 25, 80, 17);
		
		Label lblNewLabel_3 = new Label(grpcd, SWT.NONE);
		lblNewLabel_3.setAlignment(SWT.RIGHT);
		lblNewLabel_3.setBounds(10, 85, 80, 17);
		lblNewLabel_3.setText("上传方式：");
		
		Button btnRadioButton = new Button(grpcd, SWT.RADIO);
		btnRadioButton.setSelection(true);
		btnRadioButton.setBounds(96, 85, 97, 17);
		btnRadioButton.setText("全部上传");
		
		Label label = new Label(grpcd, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setBounds(10, 115, 80, 17);
		label.setText("文件选择：");
		
		txtsavesonglistURL = new Text(grpcd, SWT.BORDER);
		txtsavesonglistURL.setBounds(95, 22, 400, 23);
		formToolkit.adapt(txtsavesonglistURL, true, true);
		txtsavesonglistURL.setText("http://localhost:8080/song1/api/book/v1/savebooklist");
		
		text_2 = new Text(grpcd, SWT.BORDER | SWT.READ_ONLY);
		text_2.setBounds(95, 112, 400, 23);
		formToolkit.adapt(text_2, true, true);
		
		Label lblNewLabel_5 = new Label(grpcd, SWT.NONE);
		lblNewLabel_5.setAlignment(SWT.RIGHT);
		lblNewLabel_5.setBounds(10, 55, 80, 17);
		lblNewLabel_5.setText("书籍URL：");
		
		txtsavesongURL = new Text(grpcd, SWT.BORDER);
		txtsavesongURL.setText("http://localhost:8080/song1/api/book/v1/savebook");
		txtsavesongURL.setBounds(95, 50, 400, 23);
		formToolkit.adapt(txtsavesongURL, true, true);
		
		Button btnNewButton_3 = new Button(this, SWT.NONE);
		btnNewButton_3.setBounds(585, 298, 145, 33);
		formToolkit.adapt(btnNewButton_3, true, true);
		btnNewButton_3.setText("开 始 上 传");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
