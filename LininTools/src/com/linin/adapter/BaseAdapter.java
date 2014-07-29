package com.linin.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author linin
 *
 */
public abstract class BaseAdapter extends android.widget.BaseAdapter implements
		Adapter {

	public int getCount() {
		try {
			return count();
		} catch (Exception e) {
			return 0;
		}
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return view(position, convertView, parent);
	}

}
