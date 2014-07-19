package com.tools.song1.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

public class BaseComposite extends Composite {
	private Map<String,Object> parentDataMap = new HashMap<String,Object>();
	public BaseComposite(Composite parent, int style) {
		super(parent, style);
	}
	public BaseComposite(Composite parent, int style,Map<String,Object> parentDataMap) {
		super(parent, style);
		this.parentDataMap = parentDataMap;
	}
	public Map<String, Object> getParentDataMap() {
		return parentDataMap;
	}
	public void setParentDataMap(Map<String, Object> parentDataMap) {
		this.parentDataMap = parentDataMap;
	}

}
