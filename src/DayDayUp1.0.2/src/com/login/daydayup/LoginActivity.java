package com.login.daydayup;

import com.function.activity.SysApplication;
import com.function.daydayup.FunctionActivity;
import com.mainactivity.daydayup.R;
import com.menu.daydayup.AboutUs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LoginActivity extends Activity {
	private Button buttonback;
	private Button btLogin;
	private Button btAdd;
	private CheckBox check;
	private EditText password;
	private SharedPreferences sp;
	private AutoCompleteTextView userName;
	private String userNameValue, passwordValue;
	private boolean isChecked = true;

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.acticity_login);
		SysApplication.getInstance().addActivity(this);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);

		btLogin = (Button) findViewById(R.id.buttonlogin);
		btAdd = (Button) findViewById(R.id.buttonregister);
		check = (CheckBox) findViewById(R.id.check);
		password = (EditText) findViewById(R.id.password);
		userName = (AutoCompleteTextView) findViewById(R.id.userName);
		sp = this.getSharedPreferences("useinfo",
				Context.MODE_WORLD_READABLE);
		check.setChecked(true);// 默認記住密碼
		
		btLogin.setOnClickListener(new buttonListener());
		btAdd.setOnClickListener(new buttonListener());
		check.setOnCheckedChangeListener(new isChecked());
		
		buttonback = (Button)findViewById(R.id.NavigateBack);
		BackListener bcListener = new BackListener();
		buttonback.setOnClickListener(bcListener);
		/*
		 * 下拉輸入框變更事件
		 */
		userName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String[] allUserName = new String[sp.getAll().size()];//  sp.getAll().size()返回的是有多少个键值对
				allUserName = sp.getAll().keySet().toArray(new String[0]);
				//  sp.getAll()返回一张hash map  
				//  keySet()得到的是a set of the keys.  
				//  hash map是由key-value组成的  
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						LoginActivity.this,
						android.R.layout.simple_dropdown_item_1line,

						allUserName);
				userName.setAdapter(adapter);
				// 設置數據適配器
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			/*
			 * 帳號變更後自動輸入相對應的密碼
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(isChecked){
					password.setText(sp
							.getString((userName.getText()).toString(), ""));
					// 自動輸入密碼
				}
			}
		});
	}

	class isChecked implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			setChecked(isChecked);
		}

	}

	class buttonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			userNameValue = userName.getText().toString();
			passwordValue = password.getText().toString();
			switch (v.getId()) {
			/**
			 * 登錄按鈕事件
			 */
			case R.id.buttonlogin: {
				if (userNameValue.equals("") || passwordValue.equals("")) {
					Toast.makeText(LoginActivity.this, "請不要讓我懷疑你的智商，親", 0)
							.show();
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Loading....", 0);
					toast.setGravity(Gravity.CENTER, 0, 0);
					// 創建圖片視圖對象
					ImageView imageView = new ImageView(getApplicationContext());
					// 設置圖片
					imageView.setImageResource(R.drawable.wait);
					// 獲得toast的佈局
					LinearLayout toastView = (LinearLayout) toast.getView();
					// 設置此佈局爲橫向的
					toastView.setOrientation(LinearLayout.HORIZONTAL);
					// 將ImageView加入到此佈局中的第一個位置
					toastView.addView(imageView, 0);
					toast.show();
					// 判斷帳號和密碼
					if (userNameValue.equals(sp.getString("userName", ""))
							&& passwordValue.equals(sp
									.getString("password", ""))) {
						Intent intent = new Intent(LoginActivity.this,
								FunctionActivity.class);
						startActivity(intent);
						Toast.makeText(LoginActivity.this, "登錄成功", 0).show();
					} else {
						Toast.makeText(LoginActivity.this, "帳號或密碼錯誤", 0).show();
					}
				}
				break;
			}
			/*
			 * 註冊按鈕點擊事件
			 */
			case R.id.buttonregister: {
				if (userNameValue.equals("") || passwordValue.equals("")) {
					Toast.makeText(LoginActivity.this, "請不要讓我懷疑你的智商，親", 0)
							.show();
					return;
				}
				new AlertDialog.Builder(LoginActivity.this)
						.setTitle("用戶註冊")
						.setMessage("確認好你的註冊信息無誤了嗎")
						.setPositiveButton("提交",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										// 記住用戶名和密碼
										Editor editor = sp.edit();
										editor.putString("userName",
												userNameValue);
										editor.putString("password",
												passwordValue);
										editor.commit();

										Intent intent = new Intent(
												LoginActivity.this,
												FunctionActivity.class);
										startActivity(intent);
										Toast.makeText(LoginActivity.this,
												"註冊成功", 0).show();
									}
								}).setNegativeButton("返回", null).show();
				break;
			}
			default:
				break;

			}
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
