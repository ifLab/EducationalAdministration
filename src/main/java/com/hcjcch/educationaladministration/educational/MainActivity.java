package com.hcjcch.educationaladministration.educational;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hcjcch.educationaladministration.activity.OauthActivity;
import com.hcjcch.educationaladministration.event.NetworkChangeEvent;

import de.greenrobot.event.EventBus;


public class MainActivity extends Activity {
    private Button login;
    private Button mark;
    private Button room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button) findViewById(R.id.login);
        mark = (Button) findViewById(R.id.mark);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, OauthActivity.class);
                startActivity(intent);
            }
        });
        room = (Button)findViewById(R.id.room);
        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SchoolPlaceActivity.class);
                startActivity(intent);
            }
        });
        EventBus.getDefault().register(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onEventMainThread(NetworkChangeEvent event) {
        if (event.isNetworkConnected()) {
            Toast.makeText(this, "网络连接", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "网络断开", Toast.LENGTH_SHORT).show();
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