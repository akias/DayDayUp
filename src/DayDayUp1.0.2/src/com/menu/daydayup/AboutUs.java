package com.menu.daydayup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.function.activity.SysApplication;
import com.mainactivity.daydayup.R;

public class AboutUs extends Activity{
	private Button buttonback;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_aboutus);
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
