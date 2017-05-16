package com.function.daydayup;
import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.function.activity.SysApplication;
import com.mainactivity.daydayup.R;

public class TestActivity extends Activity{
	private TextView tv;
	private RadioButton test1;
	private RadioButton test2;
	private RadioButton test3;
	private RadioButton test4;
	private Button submit;
	private Button next;
	private String value;
	private int random, i = 0;
	private double m = 0;
	private String tran;
	private String word;
	private int flag = 0;// 接收答案存放的單選框標籤
	private int j = 0;// 选项选取翻译循环次数
	private String st1;
	private String st2;
	private String st3;// 接收三個錯誤隨機翻譯
	private String same,temp;
	private int test = 0;//接收單選按鈕的選中指標
	private Button buttonback;
	private RadioGroup radioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_test);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);
		SysApplication.getInstance().addActivity(this);
		tv = (TextView) findViewById(R.id.word);
		radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		test1 = (RadioButton) findViewById(R.id.test1);
		test2 = (RadioButton) findViewById(R.id.test2);
		test3 = (RadioButton) findViewById(R.id.test3);
		test4 = (RadioButton) findViewById(R.id.test4);
		submit = (Button) findViewById(R.id.submit);
		next = (Button)findViewById(R.id.next);
		
		RadioGroupListener listener = new RadioGroupListener();
		radioGroup.setOnCheckedChangeListener(listener);

		submit.setOnClickListener(new buttonListener());
		next.setOnClickListener(new buttonListener());
		
		buttonback = (Button) findViewById(R.id.NavigateBack);
		BackListener bcListener = new BackListener();
		buttonback.setOnClickListener(bcListener);

		// 在工程的res目录下面新建一个dictionary文件夹，把cet4.xml拖进去
		XmlResourceParser xrp = getResources().getXml(R.xml.first);// 定义一个XML资源解析器
		try {
			StringBuilder strbuilder = new StringBuilder("");
			// m = Math.random()*100+Math.random()*100;
			while ((xrp.getEventType() != XmlResourceParser.END_DOCUMENT)) {// 当还没到XML文档结束时
				while (m < 5) {
					m = Math.random() * 100 + Math.random() * 100;
				}
				for (int i = 0; i < m; i++) {
					if (xrp.getEventType() == XmlResourceParser.START_TAG) {// 当遇到开始标签时,包括子标签和子标签里面的所有标签
						String tagname = xrp.getName();// 获取标签的名字
						if (tagname.equals("word")) {
							value = xrp.nextText();
							xrp.next();
							tran = xrp.nextText();// 取出翻译
						}
					}
					xrp.next();
				}
				break;
			}
			tv.setText(value);
			random = (int) (Math.random() * 10) / 3 + 1;
			switch (random) {
			case 1:
				test1.setText(tran);
				flag = 1;
				break;
			case 2:
				test2.setText(tran);
				flag = 2;
				break;
			case 3:
				test3.setText(tran);
				flag = 3;
				break;
			case 4:
				test4.setText(tran);
				flag = 4;
				break;
			}
			
			same = tran;
			while (j < 3) {
				tran = null;
				xrp = getResources().getXml(R.xml.first);
				while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {// 当还没到XML文档结束时
					m=0;
					while (m < 8) {
						m = Math.random() * 100 + Math.random() * 136;//236是能遍歷到最後一個翻譯的最小隨機數
					}
					for (int i = 0; i < m; i++) {
						if (xrp.getEventType() == XmlResourceParser.START_TAG) {// 当遇到开始标签时,包括子标签和子标签里面的所有标签
							String tagname1 = xrp.getName();// 获取标签的名字
							if (tagname1.equals("trans")) {
								tran = xrp.nextText();
							}
						}
						xrp.next();
					}
					break;
				}
				System.out.println(m);
				System.out.println(j+tran);
				if((tran.equals(same))||(tran.equals(st1))||(tran.equals(st2))){
					System.out.println("相同");
					continue;
				}
				switch (j) {
				case 0: {
					st1 = tran;
					break;
				}
				case 1: {
					st2 = tran;
					break;
				}
				case 2: {
					st3 = tran;
					break;
				}
				}

				j++;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		switch (flag) {
		case 1: {
			test2.setText(st1);
			test3.setText(st2);
			test4.setText(st3);
			break;
		}
		case 2: {
			test1.setText(st1);
			test3.setText(st2);
			test4.setText(st3);
			break;
		}
		case 3: {
			test1.setText(st1);
			test2.setText(st2);
			test4.setText(st3);
			break;
		}
		case 4: {
			test1.setText(st1);
			test2.setText(st2);
			test3.setText(st3);
			break;
		}
		}

	}
	//菜單監聽
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class RadioGroupListener implements OnCheckedChangeListener{

		//checkedId是选中的Button id
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch(checkedId){
			case R.id.test1:
				test=1;
				break;
			case R.id.test2:
				test =2;
				break;
			case R.id.test3:
				test =3;
				break;
			case R.id.test4:
				test =4;
				break;
			}
		}

	}
	
	class buttonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.submit:{
					if(test==flag){
						Toast.makeText(TestActivity.this, "DayDayUp~~！！", 0).show();
						finish();
						Intent intent = new Intent(TestActivity.this,
								TestActivity.class);
						startActivity(intent);
						Toast.makeText(TestActivity.this, "下一題", 0).show();
					}
					else{
						Toast toast =Toast.makeText(getApplicationContext(),
								"原來你是在猜啊！！！", 0);
						toast.setGravity(Gravity.CENTER, 0, 0);
						// 創建圖片視圖對象
						ImageView imageView = new ImageView(getApplicationContext());
						// 設置圖片
						imageView.setImageResource(R.drawable.wrongsign);
						// 獲得toast的佈局
						LinearLayout toastView = (LinearLayout) toast.getView();
						// 設置此佈局爲橫向的
						toastView.setOrientation(LinearLayout.HORIZONTAL);
						// 將ImageView加入到此佈局中的第一個位置
						toastView.addView(imageView, 0);
						toast.show();
					}
					break;	
			}
			
			case R.id.next:{
			}
			}
		}
		
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