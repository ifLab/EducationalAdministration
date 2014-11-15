package com.hcjcch.educationaladministration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.hcjcch.educationaladministration.educational.R;
import com.hcjcch.educationaladministration.event.NetworkChangeEvent;
import com.hcjcch.educationaladministration.utils.MarkUtils;


import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by limbo on 2014/10/26.
 */
public class MarkDetailActivity extends Activity {
    //listActivity
    private  ListView listview = null;
    private String id = null;
    private String year=null; //学年
    private String semester = null; //学期
    private String type = null; //类型
    private Intent intent = null;
    private MarkUtils markUtils = null;
    private List<Map<String,Object>> list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_detail);
        listview = (ListView)findViewById(R.id.detail);
        //获取传递的信息
        intent = getIntent();
        id = intent.getStringExtra("id");
        year = intent.getStringExtra("year");
        semester = intent.getStringExtra("semester");
        type = intent.getStringExtra("type");
        //xuehao = MarkUtils.getxuehao();
        markUtils = new MarkUtils(this.id,this,year,semester,type);
        listview.setDividerHeight(0);//取消分割线
        list = markUtils.get_list();
        if(list.size() == 0){
            show_error();
        }
        Log.i("2","2");
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.mark_detail_item,
            new String[]{"kcmc", "pscj", "qmcj", "sycj", "qzcj", "cj", "xf", "gd"},
            new int[]{R.id.kcmc, R.id.pscj, R.id.qmcj, R.id.sycj, R.id.qzcj, R.id.cj, R.id.xf, R.id.gd});
        listview.setAdapter(adapter);

        EventBus.getDefault().register(this);
    }

    private void show_error(){
        Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
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
