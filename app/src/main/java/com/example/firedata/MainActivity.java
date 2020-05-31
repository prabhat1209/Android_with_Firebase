package com.example.firedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    public static final String ARTIST_NAME = "artistName";
    public static final String ARTIST_ID = "artistId";

    EditText e;
    Button b;
    Spinner sp;
    DatabaseReference databaseArtist;
    ListView lv;
    List<Artist> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtist = FirebaseDatabase.getInstance().getReference("artists");

        e = findViewById(R.id.et);
        b = findViewById(R.id.btn);
        sp = findViewById(R.id.spin);
        lv = findViewById(R.id.li);
        list = new ArrayList<>();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = list.get(position);
                Intent in = new Intent(getApplicationContext(),AddTrackActivity.class);
                in.putExtra(ARTIST_ID,artist.getId());
                in.putExtra(ARTIST_NAME,artist.getName());
                startActivity(in);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Artist ar = list.get(position);
                showUpdateDialog(ar.getId(),ar.getName());
                return true;
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot as : dataSnapshot.getChildren()){
                    Artist artist = as.getValue(Artist.class);
                    list.add(artist);
                }

                ArtistList adapter = new ArtistList(MainActivity.this,list);
                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String artistId, String artistName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.na);
        final Spinner spinnerGenre = (Spinner) dialogView.findViewById(R.id.spin1);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.bun);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteArtist);

        dialogBuilder.setTitle(artistName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String genre = spinnerGenre.getSelectedItem().toString();
                if (!TextUtils.isEmpty(name)) {
                    updateArtist(artistId, name, genre);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(artistId);
            }
        });
     }

     private boolean deleteArtist(String id){
         DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);
         dR.removeValue();

         DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);
         drTracks.removeValue();
         Toast.makeText(getApplicationContext(), "Artist Deleted", Toast.LENGTH_LONG).show();

         return true;
     }
    private boolean updateArtist(String id, String name, String genre) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);

        //updating artist
        Artist artist = new Artist(id, name, genre);
        dR.setValue(artist);
        Toast.makeText(getApplicationContext(), "Artist Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    public void add(){
        String name = e.getText().toString();
        String genre = sp.getSelectedItem().toString();
        if(!TextUtils.isEmpty(name)){

            String id = databaseArtist.push().getKey();
            Artist art = new Artist(id, name, genre);
            databaseArtist.child(id).setValue(art);
            Toast.makeText(getApplicationContext(),"Artist added Successfully",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Enter a valid name.",Toast.LENGTH_SHORT).show();
        }
    }

}
