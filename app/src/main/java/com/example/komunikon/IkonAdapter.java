package com.example.komunikon;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IkonAdapter extends ArrayAdapter<Ikon> {
    private Context context;
    private int resourceId;
    private List<Ikon> items, tempItems, suggestions;

    public IkonAdapter(@NonNull Context context, int resourceId, @NonNull List<Ikon> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            Ikon ikon = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.textView);
            name.setText(ikon.getName());

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageResource(ikon.getImage());

            String inputStr = ikon.getMatchedWord();
            TextView suggestion = (TextView) view.findViewById(R.id.textViewSuggestion);

            String suggestionStr = "";
            for (String meaning: ikon.getMeanings()){
                if (meaning.startsWith(inputStr)){
                    suggestionStr = meaning.substring(inputStr.length());
                    break;
                }
            }


            suggestion.setText(": ("+inputStr+suggestionStr+")");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public Ikon getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return ikonFilter;
    }

    private Filter ikonFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Ikon ikon = (Ikon) resultValue;
            return ikon.getName();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                String inputWord = charSequence.toString().toLowerCase();
                suggestions.clear();
                for (Ikon ikon: tempItems) {
                    for (String meaning: ikon.getMeanings()){
                        if (meaning.toLowerCase().startsWith(inputWord)) {
                            ikon.setMatchedWord(inputWord);
                            suggestions.add(ikon);
                            break;
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<Ikon> tempValues = (ArrayList<Ikon>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (Ikon ikonObj : tempValues) {
                    add(ikonObj);
                }
                notifyDataSetChanged();
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}
