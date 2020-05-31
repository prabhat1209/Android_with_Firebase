package com.example.firedata;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ArtistList extends ArrayAdapter<Artist> {

    private Activity context;
    private List<Artist> list;
    public ArtistList(Activity context, List<Artist> list){
        super(context,R.layout.list_layout,list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);
        TextView tv1 = (TextView) listViewItem.findViewById(R.id.n);
        TextView tv2 = (TextView) listViewItem.findViewById(R.id.g);

        Artist artist = list.get(position);
        tv1.setText(artist.getName());
        tv2.setText(artist.getGenre());
        return listViewItem;
    }
}
