package com.function.daydayup;


import com.function.activity.SysApplication;

import com.mainactivity.daydayup.R;


import com.menu.daydayup.AboutUs;
import com.menu.daydayup.Help;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class FunctionActivity extends Activity implements OnTouchListener{
	private ImageButton btaddwords;
	private ImageButton bttest;
	private ImageButton btlearn;
	private ImageButton btsearch;
	private ImageButton btlaugh;
	private ImageButton btbook;
	
	private AudioManager audioManager=null;
	private Button buttonexit;
	private Button buttonaboutus;
	private Button buttonsetsound;
	private Button buttonhelp;
	private Button buttonback;
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
	private VelocityTracker mVelocityTracker;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_function);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);
		SysApplication.getInstance().addActivity(this);
		
		bttest = (ImageButton)findViewById(R.id.bttest);
		btlearn = (ImageButton)findViewById(R.id.btlearn);
		btsearch = (ImageButton)findViewById(R.id.btsearch);
		btlaugh = (ImageButton)findViewById(R.id.btlaugh);
		SearchListener searchListener = new SearchListener();
		btsearch.setOnClickListener(searchListener);
		
		LearnListener learnListener = new LearnListener();
		btlearn.setOnClickListener(learnListener);
		
		TestListener testListener = new TestListener();
		bttest.setOnClickListener(testListener);
		
		LaughListener laughListener = new LaughListener();
		btlaugh.setOnClickListener(laughListener);
		
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
        //帮组监听器
        buttonhelp = (Button)findViewById(R.id.buttonhelp);
        HelpListener helpListener = new HelpListener();
        buttonhelp.setOnClickListener(helpListener);
        
        buttonback = (Button)findViewById(R.id.NavigateBack);
		BackListener bcListener = new BackListener();
		buttonback.setOnClickListener(bcListener);
        
	}
	/*
	 * heop进入帮组监听器类
	 */
		class HelpListener implements OnClickListener {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(FunctionActivity.this, Help.class);
				startActivity(intent);
			}
		}
		/*
		 * title返回监听器
		 */
		class BackListener implements OnClickListener {

			public void onClick(View v) {
				finish();
			}
		}
	class SearchListener implements OnClickListener{

		
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(FunctionActivity.this,SearchActivity.class);
			startActivity(intent);
			}
		}
class LearnListener implements OnClickListener{

		
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(FunctionActivity.this,LearnActivity.class);
			startActivity(intent);
			}
		}
class TestListener implements OnClickListener{

	
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(FunctionActivity.this,TestActivity.class);
		startActivity(intent);
		}
	}
class LaughListener implements OnClickListener{
	
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(FunctionActivity.this,LaughActivity.class);
		startActivity(intent);
		}
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
		new AlertDialog.Builder(FunctionActivity.this)
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
		intent.setClass(FunctionActivity.this, AboutUs.class);
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
}
