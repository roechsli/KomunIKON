package com.example.myfirstapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/*public class SentenceListAdapter extends ArrayAdapter {
    private final Activity context;
    private final List<Integer> sentenceImageID;

    public SentenceListAdapter(Activity context, List<Integer> sentenceImageID){
        super(R.layout.sentence_list_row, sentenceImageID);
        this.context = context;
        this.sentenceImageID = sentenceImageID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.sentence_list_row, null);

        ImageView sentenceImage = (ImageView) rowView.findViewById(R.id.sentenceRowImage);
        sentenceImage.setImageResource(sentenceImageID.get(position));
        return rowView;
    }
}*/
