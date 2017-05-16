package com.function.daydayup;

import org.w3c.dom.NodeList;

import com.function.activity.SysApplication;
import com.function.daydayup.FunctionActivity.BackListener;
import com.mainactivity.daydayup.R;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UnitLearn extends Activity{
	private TextView tv;
	private String st1="",st2="";
	private Button buttonback;
	NodeList item;
	
	public NodeList getItem() {
		return item;
	}
	public void setItem(NodeList item) {
		this.item = item;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_unit);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);
		SysApplication.getInstance().addActivity(this);
		
		buttonback = (Button)findViewById(R.id.NavigateBack);
		BackListener bcListener = new BackListener();
		buttonback.setOnClickListener(bcListener);
		
		tv = (TextView)findViewById(R.id.tv);
		XmlResourceParser xrp = null;
		LearnActivity la = new LearnActivity();
		switch(la.getFlag()){
		case 1:xrp = getResources().getXml(R.xml.it1);break;// 定义一个XML资源解析器 2:
		case 2:xrp = getResources().getXml(R.xml.it2);break;
		case 3:xrp = getResources().getXml(R.xml.it3);break;
		case 4:xrp = getResources().getXml(R.xml.it4);break;
		case 5:xrp = getResources().getXml(R.xml.it5);break;
		}
		try {
			StringBuilder strbuilder = new StringBuilder("");
			while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {// 当还没到XML文档结束时
				if (xrp.getEventType() == XmlResourceParser.START_TAG) {// 当遇到开始标签时,包括子标签和子标签里面的所有标签
					String tagname = xrp.getName();// 获取标签的名字

					if (tagname.equals("word")) {// 取出单词
						strbuilder.append(xrp.nextText());
						strbuilder.append("\n");// 读取标签里面内容，也就是这个单词
							xrp.next();// 读取这个下一个标签，也就是<trans>翻译
						strbuilder.append(xrp.nextText());
						strbuilder.append("\n\n");// 取出翻译
					}
				}
				xrp.next();// 读取下一个标签
			}
			tv.setText(strbuilder);
		} catch (Exception e) {
			// TODO: handle exception
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
}
