package com.hcjcch.educationaladministration.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import com.hcjcch.educationaladministration.config.StaticVariable;
import com.hcjcch.educationaladministration.educational.R;
import com.hcjcch.educationaladministration.event.NetworkChangeEvent;
import com.hcjcch.educationaladministration.utils.EduHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import de.greenrobot.event.EventBus;

/**
 * Created by limbo on 2014/10/26.
 */
public class MarkQueryActivity extends Activity {
    public static String[] years = new String[4];//存储学年列表
    private EditText year = null;
    private EditText semester = null;//学期
    private EditText type = null;
    private Button querybutton = null;
    private String xuehao = "2012011141"; //学号请自行修改
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_query);
        year = (EditText)findViewById(R.id.EditView1);
        year.setFocusable(false);
        // not show the Keyboard
        semester = (EditText)findViewById(R.id.EditView2);
        semester.setFocusable(false);
        type = (EditText)findViewById(R.id.EditView3);
        type.setFocusable(false);
        querybutton = (Button)findViewById(R.id.queryButton);
        get_years("year.php");// 获取学年列表;这么写会不会暴露api的地址？
        // TODO

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        querybutton.setBackgroundResource(R.drawable.searchbutton);
    }

    public void input_year(View view){
        //显示学年列表；

        final CharSequence a[] = new CharSequence[4];
        if((years[0]!=null)&&(years[1]!=null)&&(years[2]!=null)&&(years[3]!=null)){
            a[0]=years[0];
            a[1]=years[1];
            a[2]=years[2];
            a[3]=years[3];
            //确保years里面存储有干货
            new AlertDialog.Builder(this).setTitle("选择学年").setItems(a, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    year.setText(a[which]);
                }
            }).show();//显示列表，a必须是final？？？？？？why
        }else{
            show_error();
        }

    }

    public void input_semester(View view){
        //这个是学期。。。。。。
        final CharSequence[] a = new CharSequence[2];
        a[0] = "1";
        a[1] = "2";
        new AlertDialog.Builder(this).setTitle("选择学期").setItems(a,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                semester.setText(a[which]);
            }
        }).show();
    }

    public void input_type(View view){
        //这个是类型；
        //一部分固定的字符串存在StaticVariable
        final CharSequence[] a = new CharSequence[6];
        a[0] = StaticVariable.qbkc;
        a[1] = StaticVariable.ggjck;
        a[2] = StaticVariable.zyjck;
        a[3] = StaticVariable.ggxxk;
        a[4] = StaticVariable.zyxxk;
        a[5] = StaticVariable.sjk;
        new AlertDialog.Builder(this).setTitle("选择课程类型").setItems(a,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                type.setText(a[which]);
            }
        }).show();
    }

    public void query(View view){
        view.setBackgroundResource(R.drawable.searchedbutton);
        if(!is_void()) {
            Intent intent = new Intent();
            intent.setClass(MarkQueryActivity.this, MarkDetailActivity.class);
            intent.putExtra("year",year.getText().toString() );
            intent.putExtra("semester", semester.getText().toString());//学期
            intent.putExtra("type", type.getText().toString());
            intent.putExtra("id",xuehao);
            //窗口跳转 、传值
            startActivity(intent);
        }else{
            Toast.makeText(this, "选项不能为空",Toast.LENGTH_SHORT).show();
            view.setBackgroundResource(R.drawable.searchbutton);
        }
    }

    private void get_years(String url){
        //使用api获取学年列表，
        RequestParams params = new RequestParams();
        params.add("xh",xuehao);
        //api_path;
        //异步获取json并解析json
        EduHttpClient.get(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onStart(){
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //execution;
                String json = new String(responseBody);
                try {
                    //!!!decode JSON must try block!!!!
                    JSONArray array = new JSONArray(json);
                    for(int i=0;i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        MarkQueryActivity.years[i]=object.getString("title");
                    }

                }catch (JSONException e){
                    show_error();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //show the error
                show_error();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
        //return a;
    }

    private void show_error(){
        Toast.makeText(this, "无法获取信息", Toast.LENGTH_SHORT).show();
    }

    private boolean is_void(){
        boolean res = false;
        if(type.getText().toString().equals("")||year.getText().toString().equals("")||semester.getText().toString().equals(""))
            res = true;
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onEventMainThread(NetworkChangeEvent event){
        if (event.isNetworkConnected()){
            Toast.makeText(this, "网络连接", Toast.LENGTH_SHORT).show();
        }  else {
            Toast.makeText(this,"网络断开",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
