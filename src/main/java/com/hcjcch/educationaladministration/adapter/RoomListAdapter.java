package com.hcjcch.educationaladministration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcjcch.educationaladministration.educational.R;

import java.util.ArrayList;

/**
 * Created by pangrong on 2014/11/5.
 */
public class RoomListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> rooms;

    public RoomListAdapter(Context context,ArrayList<String> rooms){
        this.context = context;
        this.rooms = rooms;
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public Object getItem(int i) {
        return rooms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class ViewHodler{
        public TextView text;
        public ImageView[] image = new ImageView[12];
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHodler hodler;
        if (view == null) {
            hodler = new ViewHodler();
            view = LayoutInflater.from(context).inflate(R.layout.class_item,null);
            hodler.text = (TextView)view.findViewById(R.id.roomNames);
            hodler.image[0] = (ImageView)view.findViewById(R.id.One);
            hodler.image[1] = (ImageView)view.findViewById(R.id.Two);
            hodler.image[2] = (ImageView)view.findViewById(R.id.Three);
            hodler.image[3] = (ImageView)view.findViewById(R.id.Four);
            hodler.image[4] = (ImageView)view.findViewById(R.id.Fifth);
            hodler.image[5] = (ImageView)view.findViewById(R.id.Six);
            hodler.image[6] = (ImageView)view.findViewById(R.id.Seven);
            hodler.image[7] = (ImageView)view.findViewById(R.id.Eight);
            hodler.image[8] = (ImageView)view.findViewById(R.id.Nine);
            hodler.image[9] = (ImageView)view.findViewById(R.id.Ten);
            hodler.image[10] = (ImageView)view.findViewById(R.id.Eleven);
            hodler.image[11] = (ImageView)view.findViewById(R.id.shier);
            view.setTag(hodler);
        }else {
            hodler = (ViewHodler)view.getTag();
        }
        hodler.text.setText(rooms.get(i));
        return view;
    }
}
