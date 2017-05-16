package com.function.daydayup;

import java.util.Locale;

import com.anbyke.service.FileService;
import com.function.activity.SysApplication;
import com.mainactivity.daydayup.R;
import com.menu.daydayup.AboutUs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnInitListener,OnTouchListener{
	private String input;
	private String match;
	private EditText et;
	private ImageButton search;
	private Button del;
	private TextView display;
	private Button add;
	private int flag;//判斷是否查找到單詞
	
	private TextToSpeech speak = null;//定义tts
	private Button speakbt = null;//tts发音按钮
	private String speakstr = "";//发音文本

	//菜單部份聲明
	private AudioManager audioManager=null;
	private Button buttonexit;
	private Button buttonaboutus;
	private Button buttonsetsound;
	public static final int SNAP_VELOCITY = 200;               
	private int screenWidth;
	private int leftEdge;
	private int rightEdge = 0;
	private int menuPadding = 80;
	private View content;
	private View menu;
	private LinearLayout.LayoutParams menuParams;
	private float xDown;
	private float xMove;
	private float xUp;
	private boolean isMenuVisible;
	private Button buttonback;
	private VelocityTracker mVelocityTracker;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_search);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);
		SysApplication.getInstance().addActivity(this);
		et = (EditText) findViewById(R.id.et);
		search = (ImageButton) findViewById(R.id.search);
		del = (Button) findViewById(R.id.del);
		add = (Button) findViewById(R.id.add);
		display = (TextView) findViewById(R.id.tv);
		speakbt = (Button) findViewById(R.id.speak);
		//菜單部份創建類對象以及為其添加監聽器
		initValues();
		content.setOnTouchListener(this);
		audioManager=(AudioManager)getSystemService(Service.AUDIO_SERVICE);
		
		//菜单中退出程序的监听器
		buttonexit = (Button)findViewById(R.id.buttonexit);
		ExitListener exitListener = new ExitListener();
        buttonexit.setOnClickListener(exitListener);
        //菜单中关于我们的监听器
        buttonaboutus = (Button)findViewById(R.id.btus);
        AboutUsListener aboutUsListener = new AboutUsListener();
        buttonaboutus.setOnClickListener(aboutUsListener);
        
        //声音设置监听器
        buttonsetsound=(Button)findViewById(R.id.buttonsetsound);
        buttonsetsound.setOnClickListener(listener);
        

		buttonback = (Button) findViewById(R.id.NavigateBack);
		BackListener bcListener = new BackListener();
		buttonback.setOnClickListener(bcListener);

	
		speak = new TextToSpeech(this,  this);//new只是，系统会调用tts的初始化函数onInit()。所有要复写onInit()方法

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stubge
				flag = 0;
				input = et.getText().toString();
				// 在工程的res目录下面新建一个dictionary文件夹，把cet4.xml拖进去
				XmlResourceParser xrp = getResources().getXml(R.xml.cet4);// 定义一个XML资源解析器

				try {
					StringBuilder strbuilder = new StringBuilder("");
					while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {// 当还没到XML文档结束时
						if (xrp.getEventType() == XmlResourceParser.START_TAG) {// 当遇到开始标签时,包括子标签和子标签里面的所有标签
							String tagname = xrp.getName();// 获取标签的名字

							if (tagname.equals("word")) {// 取出单词
								match = xrp.nextText();// 读取标签里面内容，也就是这个单词

								if (match.equalsIgnoreCase(input)) {// 如果取出来的这个单词和输入的单词相等
									xrp.next();// 读取这个下一个标签，也就是<trans>翻译
									strbuilder.append(xrp.nextText());// 取出翻译
									display.setText(strbuilder);// 显示翻译
									System.out.println(strbuilder);
									speakstr=match;//赋值给发音文本
									flag = 1;
									break;
								}
							}
						}
						xrp.next();// 读取下一个标签
					}
					if(flag==0){
						display.setText("親。。小白告訴你 這不是四級單詞");
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
		
		speakbt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				speak.speak(speakstr, TextToSpeech.QUEUE_ADD, null);

			}
		});
		del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display.setText("");
				et.setText("");
				speakstr="";

			}
		});
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FileService service = new FileService(getApplicationContext());
				try {
					//service.save(name,content);
					// filereadText.setText(service.read("all.txt").toString());
					 //判断SDcard 是否存在
					 if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
						 service.saveToSDCard("words.txt", match);
						 Toast.makeText(getApplicationContext(),"成功加入",1).show();
					 }else{
						 Toast.makeText(getApplicationContext(),"sdcard不存在",1).show();
					 }
				} catch (Exception e) {
					 Toast.makeText(getApplicationContext(),"操作异常",1).show();
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
		speak.setLanguage(Locale.US);// 初始化TTS组件，设置语言为US英语
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (speak != null)
			speak.shutdown();//关闭tts引擎
	
}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void initValues() {
		WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		screenWidth = window.getDefaultDisplay().getWidth();
		content = findViewById(R.id.content);
		menu = findViewById(R.id.menu);
		menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
		// 将menu的宽度设置为屏幕宽度减去menuPadding
		menuParams.width = screenWidth - menuPadding;
		// 左边缘的值赋值为menu宽度的负数
		leftEdge = -menuParams.width;
		// menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见
		menuParams.leftMargin = leftEdge;
		// 将content的宽度设置为屏幕宽度
		content.getLayoutParams().width = screenWidth;
	}


	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指按下时，记录按下时的横坐标
			xDown = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			// 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu
			xMove = event.getRawX();
			int distanceX = (int) (xMove - xDown);
			if (isMenuVisible) {
				menuParams.leftMargin = distanceX;
			} else {
				menuParams.leftMargin = leftEdge + distanceX;
			}
			if (menuParams.leftMargin < leftEdge) {
				menuParams.leftMargin = leftEdge;
			} else if (menuParams.leftMargin > rightEdge) {
				menuParams.leftMargin = rightEdge;
			}
			menu.setLayoutParams(menuParams);
			break;
		case MotionEvent.ACTION_UP:
			// 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
			xUp = event.getRawX();
			if (wantToShowMenu()) {
				if (shouldScrollToMenu()) {
					scrollToMenu();
				} else {
					scrollToContent();
				}
			} else if (wantToShowContent()) {
				if (shouldScrollToContent()) {
					scrollToContent();
				} else {
					scrollToMenu();
				}
			}
			recycleVelocityTracker();
			break;
		}
		return true;
	}

	/**
	 * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。
	 * 
	 * @return 当前手势想显示content返回true，否则返回false。
	 */
	private boolean wantToShowContent() {
		return xUp - xDown < 0 && isMenuVisible;
	}

	/**
	 * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是正数，且当前menu是不可见的，则认为当前手势是想要显示menu。
	 * 
	 * @return 当前手势想显示menu返回true，否则返回false。
	 */
	private boolean wantToShowMenu() {
		return xUp - xDown > 0 && !isMenuVisible;
	}

	/**
	 * 判断是否应该滚动将menu展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY，
	 * 就认为应该滚动将menu展示出来。
	 * 
	 * @return 如果应该滚动将menu展示出来返回true，否则返回false。
	 */
	private boolean shouldScrollToMenu() {
		return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 判断是否应该滚动将content展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2，
	 * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将content展示出来。
	 * 
	 * @return 如果应该滚动将content展示出来返回true，否则返回false。
	 */
	private boolean shouldScrollToContent() {
		return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 将屏幕滚动到menu界面，滚动速度设定为30.
	 */
	private void scrollToMenu() {
		new ScrollTask().execute(30);
	}

	/**
	 * 将屏幕滚动到content界面，滚动速度设定为-30.
	 */
	private void scrollToContent() {
		new ScrollTask().execute(-30);
	}

	/**
	 * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
	 * 
	 * @param event
	 *            content界面的滑动事件
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 获取手指在content界面滑动的速度。
	 * 
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = menuParams.leftMargin;
			// 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
			while (true) {
				leftMargin = leftMargin + speed[0];
				if (leftMargin > rightEdge) {
					leftMargin = rightEdge;
					break;
				}
				if (leftMargin < leftEdge) {
					leftMargin = leftEdge;
					break;
				}
				publishProgress(leftMargin);
				// 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。
				sleep(20);
			}
			if (speed[0] > 0) {
				isMenuVisible = true;
			} else {
				isMenuVisible = false;
			}
			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			menuParams.leftMargin = leftMargin[0];
			menu.setLayoutParams(menuParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			menuParams.leftMargin = leftMargin;
			menu.setLayoutParams(menuParams);
		}
	}

	/**
	 * 使当前线程睡眠指定的毫秒数。
	 * 
	 * @param millis
	 *            指定当前线程睡眠多久，以毫秒为单位
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 退出程序的监听器类
	 */
	class ExitListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//android.os.Process.killProcess(android.os.Process.myPid());
			new AlertDialog.Builder(SearchActivity.this)
			.setTitle("友情提示")
			.setMessage("確定今天你的學習計劃完成了嗎")
			.setPositiveButton("休息一下", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							finish();
						}
					}).setNegativeButton("繼續學習", null).show();
		}
		
	}
	/*
	 * 关于我们监听器类
	 */
	class AboutUsListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SearchActivity.this, AboutUs.class);
			startActivity(intent);
		}
	}
	/*
	 * 声音设置
	 */
	 View.OnClickListener listener=new View.OnClickListener(){
	        public void onClick(View v) {
	            @SuppressWarnings("unused")
	            Button btn=(Button)v;
	           
	                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
	                        AudioManager.ADJUST_RAISE, 
	                        AudioManager.FLAG_SHOW_UI);    //调高声音

	            }
};

/*
 * 菜单返回监听器类
 */
class BackListener implements OnClickListener {

	public void onClick(View v) {
		finish();
	}
}
}