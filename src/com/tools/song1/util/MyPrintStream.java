/**
 * 
 */
package com.tools.song1.util;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * @author Administrator
 *
 */
public class MyPrintStream extends PrintStream {
	private Text text;  
	  
    public MyPrintStream(OutputStream out, Text text) {  
        super(out);  
        this.text = text;  
    }  
    // 重写父类write方法,这个方法是所有打印方法里面都要调用的方法  
    public void write(byte[] buf, int off, int len) {  
        final String message = new String(buf, off, len);  
  
        // SWT非界面线程访问组件的方式  
        Display.getDefault().syncExec(new Thread() {  
            public void run() {  
                // 把信息添加到组件中  
                if (text != null && !text.isDisposed()) {  
                    text.append(message);  
                }  
            }  
        });  
    }  
}
