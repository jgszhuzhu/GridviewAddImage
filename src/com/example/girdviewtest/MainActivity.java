package com.example.girdviewtest;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore;

public class MainActivity extends Activity {

	private GridView gridView1;                   //������ʾ����ͼ
	private Button buttonPublish;                //������ť
	private final int IMAGE_OPEN = 1;        //��ͼƬ���
	private String pathImage;                       //ѡ��ͼƬ·��
	private Bitmap bmp;                               //������ʱͼƬ
	private ArrayList<HashMap<String, Object>> imageItem;
	private SimpleAdapter simpleAdapter;     //������

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * ��ֹ���̵�ס�����
         * ��ϣ���ڵ�����activity���� android:windowSoftInputMode="adjustPan"
         * ϣ����̬�����߶� android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
        		SOFT_INPUT_ADJUST_PAN);
        //������Ļ
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        //��ȡ�ؼ�����
        gridView1 = (GridView) findViewById(R.id.gridView1);

        /*
         * ����Ĭ��ͼƬ���ͼƬ�Ӻ�
         * ͨ��������ʵ��
         * SimpleAdapter����imageItemΪ����Դ R.layout.griditem_addpicΪ����
         */
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic); //�Ӻ�
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, 
        		imageItem, R.layout.griditem_addpic, 
                new String[] { "itemImage"}, new int[] { R.id.imageView1}); 
        /*
         * HashMap����bmpͼƬ��GridView�в���ʾ,�������������ԴID����ʾ ��
         * map.put("itemImage", R.drawable.img);
         * �������:
         *              1.�Զ���̳�BaseAdapterʵ��
         *              2.ViewBinder()�ӿ�ʵ��
         *  �ο� http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new ViewBinder() {  
		    @Override  
		    public boolean setViewValue(View view, Object data,  
		            String textRepresentation) {  
		        // TODO Auto-generated method stub  
		        if(view instanceof ImageView && data instanceof Bitmap){  
		            ImageView i = (ImageView)view;  
		            i.setImageBitmap((Bitmap) data);  
		            return true;  
		        }  
		        return false;  
		    }
        });  
        gridView1.setAdapter(simpleAdapter);
        
        /*
         * ����GridView����¼�
         * ����:�ú���������󷽷� ����Ҫ�ֶ�����import android.view.View;
         */
        gridView1.setOnItemClickListener(new OnItemClickListener() {
  			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
  				if( imageItem.size() == 10) { //��һ��ΪĬ��ͼƬ
  					Toast.makeText(MainActivity.this, "ͼƬ��9������", Toast.LENGTH_SHORT).show();
  				}
  				else if(position == 0) { //���ͼƬλ��Ϊ+ 0��Ӧ0��ͼƬ
  					Toast.makeText(MainActivity.this, "���ͼƬ", Toast.LENGTH_SHORT).show();
  					//ѡ��ͼƬ
  					Intent intent = new Intent(Intent.ACTION_PICK,       
  	                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  
  	                startActivityForResult(intent, IMAGE_OPEN);  
  	                //ͨ��onResume()ˢ������
  				}
  				else {
  					dialog(position);
  					//Toast.makeText(MainActivity.this, "�����" + (position + 1) + " ��ͼƬ", 
  					//		Toast.LENGTH_SHORT).show();
  				}
				
			}
  		});  
    }
    
    //��ȡͼƬ·�� ��ӦstartActivityForResult  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);        
        //��ͼƬ  
        if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {        
            Uri uri = data.getData();  
            if (!TextUtils.isEmpty(uri.getAuthority())) {  
                //��ѯѡ��ͼƬ  
                Cursor cursor = getContentResolver().query(  
                        uri,  
                        new String[] { MediaStore.Images.Media.DATA },  
                        null,   
                        null,   
                        null);  
                //���� û�ҵ�ѡ��ͼƬ  
                if (null == cursor) {  
                    return;  
                }  
                //����ƶ�����ͷ ��ȡͼƬ·��  
                cursor.moveToFirst();  
                pathImage = cursor.getString(cursor  
                        .getColumnIndex(MediaStore.Images.Media.DATA));  
            }
        }  //end if ��ͼƬ
    }
    
    //ˢ��ͼƬ
    @Override
	protected void onResume() {
		super.onResume();
		if(!TextUtils.isEmpty(pathImage)){
			Bitmap addbmp=BitmapFactory.decodeFile(pathImage);
			HashMap<String, Object> map = new HashMap<String, Object>();
	        map.put("itemImage", addbmp);
	        imageItem.add(map);
	        simpleAdapter = new SimpleAdapter(this, 
	        		imageItem, R.layout.griditem_addpic, 
	                new String[] { "itemImage"}, new int[] { R.id.imageView1}); 
	        simpleAdapter.setViewBinder(new ViewBinder() {  
			    @Override  
			    public boolean setViewValue(View view, Object data,  
			            String textRepresentation) {  
			        // TODO Auto-generated method stub  
			        if(view instanceof ImageView && data instanceof Bitmap){  
			            ImageView i = (ImageView)view;  
			            i.setImageBitmap((Bitmap) data);  
			            return true;  
			        }  
			        return false;  
			    }
	        }); 
	        gridView1.setAdapter(simpleAdapter);
	        simpleAdapter.notifyDataSetChanged();
			//ˢ�º��ͷŷ�ֹ�ֻ����ߺ��Զ����
	        pathImage = null;
		}
	}
    
    /*
     * Dialog�Ի�����ʾ�û�ɾ������
     * positionΪɾ��ͼƬλ��
     */
    protected void dialog(final int position) {
    	AlertDialog.Builder builder = new Builder(MainActivity.this);
    	builder.setMessage("ȷ���Ƴ������ͼƬ��");
    	builder.setTitle("��ʾ");
    	builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			imageItem.remove(position);
    	        simpleAdapter.notifyDataSetChanged();
    		}
    	});
    	builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			}
    		});
    	builder.create().show();
    }

}
