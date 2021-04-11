package com.example.bbcnews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    TextView fav_text_title;
    TextView fav_text_description;
    TextView fav_text_date;
    TextView fav_text_link;
    TextView fav_text_notes;
    Button fav_btn_link;
    Button fav_btn_delete;
    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_link);


        //get arrays from favorite titles
        final ArrayList<String> fav_title_details = getIntent().getStringArrayListExtra("title");
        ArrayList<String> fav_description_details = getIntent().getStringArrayListExtra("description");
        final ArrayList<String> fav_link_details = getIntent().getStringArrayListExtra("link");
        ArrayList<String> fav_pubDate_details = getIntent().getStringArrayListExtra("pubDate");
        ArrayList<String> fav_notes_details = getIntent().getStringArrayListExtra("notes");
        final int position = getIntent().getIntExtra("pos", 0);
        db = new DBHelper(this);

        //match arrays to text views
        fav_text_title = findViewById(R.id.fav_text_title);
        fav_text_title.setText(fav_title_details.get(position));

        fav_text_description = findViewById(R.id.fav_text_description);
        fav_text_description.setText(fav_description_details.get(position));

        fav_text_date = findViewById(R.id.fav_text_date);
        fav_text_date.setText(fav_pubDate_details.get(position));

        fav_text_link = findViewById(R.id.fav_text_link);
        fav_text_link.setText(fav_link_details.get(position));

        fav_text_link = findViewById(R.id.fav_edit_notes);
        fav_text_link.setText(fav_notes_details.get(position));

        //send you to bbc website
        fav_btn_link = findViewById(R.id.fav_btn_link);
        fav_btn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(fav_link_details.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //delete from database
        fav_btn_delete = findViewById(R.id.fav_btn_del);
        fav_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = fav_title_details.get(position);
                db.deleteData(title);
                Snackbar snackbar = Snackbar
                        .make(v,getString(R.string.del_toast), BaseTransientBottomBar.LENGTH_SHORT);
                snackbar.show();                fav_btn_delete.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_red));
                finish();
            }
        });
    }
}