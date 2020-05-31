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

public class TrackList extends ArrayAdapter<Track> {
    private Activity context;
    private List<Track> list;
    public TrackList(Activity context, List<Track> tracks){
        super(context,R.layout.layout_track_list ,tracks);
        this.context = context;
        this.list = tracks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_track_list,null,true);
        TextView tv1 = (TextView) listViewItem.findViewById(R.id.namet);
        TextView tv2 = (TextView) listViewItem.findViewById(R.id.rating);

        Track tra = list.get(position);
        tv1.setText(tra.getTrackName());
        tv2.setText(String.valueOf(tra.getTrackRating()));
        return listViewItem;
    }
}
