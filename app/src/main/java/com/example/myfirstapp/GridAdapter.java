package com.example.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private final List<String> values;
    private final List<Integer> images;
    private View view;

    GridAdapter(Context context, List<String> values, List<Integer> images) {
        this.context = context;
        this.values = values;
        this.images = images;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int i) {
        if (images.size() > i)
            return images.get(i);
        else
            return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            view = new View(context);
            view = layoutInflater.inflate(R.layout.single_ikon, null);
            ImageView ikonImage = (ImageView) view.findViewById(R.id.ikonImageView);
            TextView ikonText = (TextView) view.findViewById(R.id.ikonTextView);
            ikonImage.setImageResource(images.get(i));
            ikonText.setText(values.get(i));
        }
        return view;
    }
}
