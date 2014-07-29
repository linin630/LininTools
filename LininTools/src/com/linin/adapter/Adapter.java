package com.linin.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author linin
 *
 */
public interface Adapter {
	int count();

	View view(int position, View convertView, ViewGroup parent);
}
