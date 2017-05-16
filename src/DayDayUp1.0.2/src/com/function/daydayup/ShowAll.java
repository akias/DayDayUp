package com.function.daydayup;

import com.anbyke.service.FileService;
import com.function.activity.SysApplication;
import com.mainactivity.daydayup.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class ShowAll extends Activity { 
    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.activity_showall);// 指定布局
      
        SysApplication.getInstance().addActivity(this);
        
        TextView textView = (TextView)findViewById(R.id.text_view);   
        
        FileService service = new FileService(getApplicationContext());
        try {
			 //判断SDcard 是否存在
			 if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				 textView.setText(service.read("words.txt"));
				 Toast.makeText(getApplicationContext(),"成功读取",1).show();
			 }else{
				 Toast.makeText(getApplicationContext(),"sdcard不存在",1).show();
			 }
		} catch (Exception e) {
			 Toast.makeText(getApplicationContext(),"操作异常",1).show();
			e.printStackTrace();
		}

    }

}