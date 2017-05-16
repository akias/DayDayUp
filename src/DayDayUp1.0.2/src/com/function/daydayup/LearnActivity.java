package com.function.daydayup;

import com.function.activity.SysApplication;
import com.mainactivity.daydayup.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LearnActivity extends Activity{
	private Button buttonback;
	private Button bt1;
	private Button bt2;
	private Button bt3;
	private Button bt4;
	private Button bt5;
	public static int flag;
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_learn);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);
		SysApplication.getInstance().addActivity(this);
		
		buttonback = (Button) findViewById(R.id.NavigateBack);
		BackListener bcListener = new BackListener();
		buttonback.setOnClickListener(bcListener);
		
		bt1 = (Button)findViewById(R.id.b1);
		bt2 = (Button)findViewById(R.id.b2);
		bt3 = (Button)findViewById(R.id.b3);
		bt4 = (Button)findViewById(R.id.b4);
		bt5 = (Button)findViewById(R.id.b5);
		ButtonListener bt = new ButtonListener();
		bt1.setOnClickListener(bt);
		bt2.setOnClickListener(bt);
		bt3.setOnClickListener(bt);
		bt4.setOnClickListener(bt);
		bt5.setOnClickListener(bt);
}
	class ButtonListener implements OnClickListener {

		public void onClick(View v) {
			switch(v.getId()){
			case R.id.b1:setFlag(1);break;
			case R.id.b2:setFlag(2);break;
			case R.id.b3:setFlag(3);break;
			case R.id.b4:setFlag(4);break;
			case R.id.b5:setFlag(5);break;
			}
			Intent intent = new Intent();
			intent.setClass(LearnActivity.this,UnitLearn.class);
			startActivity(intent);
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/*
	 * 菜单返回监听器类
	 */
	class BackListener implements OnClickListener {

		public void onClick(View v) {
			finish();
		}
	}
}