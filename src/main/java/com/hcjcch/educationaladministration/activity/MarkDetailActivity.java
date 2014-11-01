package com.hcjcch.educationaladministration.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.hcjcch.educationaladministration.config.StaticVariable;
import com.hcjcch.educationaladministration.educational.R;
import com.hcjcch.educationaladministration.event.NetworkChangeEvent;
import com.hcjcch.educationaladministration.utils.EduHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.greenrobot.event.EventBus;

/**
 * Created by limbo on 2014/10/26.
 */
public class MarkDetailActivity extends ListActivity {
    private String year=null;
    private String semester = null;
    private String type = null;
    private String xuehao = null;
    private Intent intent = null;
    private ListView listView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mark_detail);
        intent = getIntent();
        year = intent.getStringExtra("year");
        semester = intent.getStringExtra("semester");
        type = intent.getStringExtra("type");
        xuehao = intent.getStringExtra("id");
        SimpleAdapter adapter = new SimpleAdapter(this,get_score("score.php"),R.layout.activity_mark_detail,
                new String[]{"kcmc","pscj","qmcj","sycj","qzcj","cj","xf","gd"},
                new int[]{R.id.kcmc,R.id.pscj,R.id.qmcj,R.id.sycj,R.id.qzcj,R.id.cj,R.id.xf,R.id.gd});
        setListAdapter(adapter);
        //TODO

        EventBus.getDefault().register(this);
    }

    private List<Map<String,Object>> get_score(String url){
        final List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        RequestParams params = new RequestParams();
        params.add("xh",xuehao);
        params.add("xn",year);
        params.add("xq",semester);
        if(!type.equals(StaticVariable.qbkc))
            params.add("kcxz",type);
        EduHttpClient.get(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                //Log.i(json,json);
                try {
                    Map<String,Object> map;
                    JSONArray array = new JSONArray(json);
                        for (int i = 0; i < array.length(); i++) {
                           JSONObject object = array.getJSONObject(i);
                            map = new HashMap<String, Object>();
                            map.put("kcmc", object.getString("kcmc"));
                            map.put("pscj", object.getString("pscj"));
                            map.put("qmcj", object.getString("qmcj"));
                            map.put("sycj", object.getString("sycj"));
                            map.put("qzcj", object.getString("qzcj"));
                            map.put("cj", object.getString("cj"));
                            map.put("xf", object.getString("xf"));
                            map.put("gd", object.getString("gd"));
                            list.add(map);
                        }
                }catch (JSONException e){
                    e.printStackTrace();
                    if(json.equals("null"))
                        object_is_null();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                show_error();
            }
        });
        return list;
    }

    private void object_is_null(){ Toast.makeText(this, "暂无本学期数据", Toast.LENGTH_SHORT).show();}

    private void show_error(){
        Toast.makeText(this , "无法获取信息", Toast.LENGTH_SHORT).show();
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
