package com.menu.daydayup;


import com.function.activity.SysApplication;
import com.mainactivity.daydayup.R;
import com.menu.daydayup.AboutUs.BackListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Help extends Activity{
	private Button buttonback;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_help);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);
		SysApplication.getInstance().addActivity(this);
		buttonback = (Button)findViewById(R.id.NavigateBack);
		BackListener bcListener = new BackListener();
		buttonback.setOnClickListener(bcListener);
	}
	class BackListener implements OnClickListener {

		public void onClick(View v) {
			finish();
		}
	}
}
