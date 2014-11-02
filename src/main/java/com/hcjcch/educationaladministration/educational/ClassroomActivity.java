package com.hcjcch.educationaladministration.educational;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

/**
 * Created by pangrong on 2014/11/1.
 */
public class ClassroomActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_place);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.menu_room_query);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();


    }
}
