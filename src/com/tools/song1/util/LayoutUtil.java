/**
 * 
 */
package com.tools.song1.util;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Administrator
 *
 */
public class LayoutUtil {

	public static void centerShell(Display display, Shell shell) {
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle shellBounds = shell.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		shell.setLocation(x, y);
	}
	
	public static void centerShell(Composite parent, Shell shell) {
		Rectangle displayBounds = parent.getBounds();
		Rectangle shellBounds = shell.getBounds();
		System.out.println(displayBounds.x);
		System.out.println(displayBounds.width);
		System.out.println(shellBounds.width);
		int x = displayBounds.x + (displayBounds.width / 2) - (shellBounds.width / 2) ;
		System.out.println(x);
		int y = displayBounds.y + (displayBounds.height / 2) - (shellBounds.height / 2);
		shell.setLocation(x, y);
	}

}
