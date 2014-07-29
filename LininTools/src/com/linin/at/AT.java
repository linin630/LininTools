package com.linin.at;

import android.os.AsyncTask;

public class AT extends AsyncTask<String, Integer, String> implements ATInterface{

	@Override
	protected String doInBackground(String... params) {
		doInBG();
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		end();
	}

	@Override
	public void doInBG() {
	}

	@Override
	public void end() {
	}
	
	public void start(){
		execute("");
	}

}
