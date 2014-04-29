package com.newer.contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.widget.SimpleCursorAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemLongClickListener {

	private static final String[] from = {"name","number"};
	private static final int[] to = {R.id.textView_name,R.id.textView_phone};
	private static final String KEY_NAME = "name";
	private static final String KEY_PHONE_NUMBER = "number";
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
	
	private int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ListView) findViewById(R.id.listView);
		data = new ArrayList<Map<String,String>>();
		
		listView.setOnItemLongClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("联系人");
		progressDialog.setMessage("正在加载联系人");
		progressDialog.setCancelable(false);
		
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMax(100);
		
		//动(线程)    设置进度的值

		new Thread(){
		public void run(){
			while(count <= 100 ){
				progressDialog.setProgress(count++);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			progressDialog.cancel();

		}}.start();
		
		progressDialog.setButton(Dialog.BUTTON_NEGATIVE, "取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				//终止任务
				dialog.dismiss();
			}
		});
		progressDialog.show();
			
		String info = "";
		Uri uri = Contacts.CONTENT_URI;
		String[] projection = {Contacts._ID,Contacts.DISPLAY_NAME,Contacts.HAS_PHONE_NUMBER};
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		String name = "";
		String number = "";
		
		while(cursor.moveToNext()){
			int id = cursor.getInt(0);
			name = cursor.getString(1);
			int hasnum = cursor.getInt(2);
				
			if (hasnum == 0) {
				
			} else {
				Uri uri2 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
				String[] projection2 = {ContactsContract.CommonDataKinds.Phone.NUMBER};
				String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
				String[] selectionArgs = {String.valueOf(id)};
				Cursor cursor2 = getContentResolver().query(uri2, projection2, selection, selectionArgs, null);
			
				cursor2.moveToNext();
				number = cursor2.getString(0);
				
				cursor2.close();
			}
			
			info = String.format("%d , %s , %d , %s \n", id,name,hasnum,number);
		
			Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
			
			HashMap<String, String> items = new HashMap<String, String>();
			items.put(KEY_NAME, name);
			items.put(KEY_PHONE_NUMBER, number);
			data.add(items);
		}
	
		cursor.close();
		
		adapter = new SimpleAdapter(this, data, R.layout.item, from, to);
		
		listView.setAdapter(adapter);
		
	
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
	}

	
	
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		
		return false;
	}
}
