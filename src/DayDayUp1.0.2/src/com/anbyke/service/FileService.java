package com.anbyke.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.os.Environment;

public class FileService {

	private Context context ;
	
	
	
	public FileService(Context context) {
		super();
		this.context = context;
	}


	/**
	 * 
	 * @param name
	 * @param content
	 */
	public void saveToSDCard(String name, String content) throws Exception{
         //获取外存储设备路径Environment.getExternalStorageDirectory()
		 File file = new File(Environment.getExternalStorageDirectory(),name+".txt");
		 FileOutputStream outStream = new FileOutputStream(file);
         outStream.write(content.getBytes());
         outStream.write(";".charAt(0));
	     outStream.close();
	
	}

	/**
	 * 读取
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public String read(String filename)throws Exception{
		
		FileInputStream inStream = context.openFileInput(filename);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0 ;
		while((len = inStream.read(buffer)) != -1){
			outStream.write(buffer,0,len);
		}
		byte[] data = outStream.toByteArray();
		return new String(data);
	}
	
	
}
