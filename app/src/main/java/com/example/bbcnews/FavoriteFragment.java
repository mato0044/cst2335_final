package com.example.bbcnews;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.nio.Buffer;
import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    TextView fav_title;
    TextView fav_description;
    TextView fav_date;
    TextView fav_link;
    Button getFav_link;
    Button getFav_del;
    DBHelper db;
    ListView fav_list;
    ArrayList<String> fav_array_title;
    ArrayList<String> fav_array_description;
    ArrayList<String> fav_array_pubDate;
    ArrayList<String> fav_array_link;
    ArrayList<String> fav_array_notes;
    ArrayAdapter<String> adapter;

    public FavoriteFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favorites ver 1.0");
        fav_list = view.findViewById(R.id.fav_view);
        fav_array_title = new ArrayList<String>();
        fav_array_description = new ArrayList<String>();
        fav_array_pubDate = new ArrayList<String>();
        fav_array_link = new ArrayList<String>();
        fav_array_notes = new ArrayList<String>();

        //send to favorite activity to view
        fav_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), FavoriteActivity.class);

                intent.putExtra("pos", position);
                intent.putStringArrayListExtra("title", (ArrayList<String>) fav_array_title);
                intent.putStringArrayListExtra("description", (ArrayList<String>) fav_array_description);
                intent.putStringArrayListExtra("link", (ArrayList<String>) fav_array_link);
                intent.putStringArrayListExtra("pubDate", (ArrayList<String>) fav_array_pubDate);
                intent.putStringArrayListExtra("notes", (ArrayList<String>) fav_array_notes);
                startActivity((intent));
            }
        });
        openAndQueryDatabase();
        return view;
    }
    //get database values
    private void openAndQueryDatabase() {
        try {
            DBHelper db = new DBHelper(this.getActivity());
            db = new DBHelper(getActivity());
            Cursor c = db.readData();
            //check if database is empty
            if (c.getCount() == 0) {
                Toast.makeText(getContext(), getString(R.string.nothing_toast), Toast.LENGTH_LONG);
                return;
            }
            //gets values
            while (c.moveToNext()) {
                fav_array_title.add(c.getString(0));
                fav_array_description.add(c.getString(1));
                fav_array_pubDate.add(c.getString(2));
                fav_array_link.add(c.getString(3));
                fav_array_notes.add(c.getString(4));
            }
            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, fav_array_title);
            fav_list.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}