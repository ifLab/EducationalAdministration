package com.hcjcch.educationaladministration.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.hcjcch.educationaladministration.adapter.RoomListAdapter;
import com.hcjcch.educationaladministration.educational.R;
import com.hcjcch.educationaladministration.utils.EduHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ClassroomActivity extends Activity{

    private ListView roomList;
    private String buildingCode,buildingName;
    private RoomListAdapter roomListAdapter;
    private ArrayList<String> roomNames;
    private ArrayList<String> jsbns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_list);
        Intent intent = getIntent();
        buildingName = intent.getStringExtra("buildingName");
        buildingCode = intent.getStringExtra("buildingCode");

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(buildingName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

        roomList = (ListView)findViewById(R.id.room_list);
        roomNames = new ArrayList<String>();
        jsbns = new ArrayList<String>();
        getroomdata();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getroomdata(){
        EduHttpClient.get("classroom.php?buildingCode="+buildingCode+"&data=2014-10-17",null,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                  String roomdata = new String(responseBody);
                try {
                    JSONArray jsonArray = new JSONArray(roomdata);
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        roomNames.add(jsonObject.getString("jsmc"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                roomListAdapter = new RoomListAdapter(ClassroomActivity.this,roomNames);
                roomList.setAdapter(roomListAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getclassdata(String jsbh){
         EduHttpClient.get("classinfo.php?jsbh="+jsbh+"&date=2014-10-16",null,new AsyncHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

             }

             @Override
             public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

             }
         });
    }

}
