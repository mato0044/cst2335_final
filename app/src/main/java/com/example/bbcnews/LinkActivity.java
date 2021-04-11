package com.example.bbcnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class LinkActivity extends AppCompatActivity {

    TextView text_title;
    TextView text_description;
    TextView text_date;
    TextView text_link;
    TextView edit_notes;
    Button button;
    Button fav_btn;
    DBHelper db;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        db = new DBHelper(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //get arrays from rss/home
        ArrayList<String> title_details = getIntent().getStringArrayListExtra("title");
        ArrayList<String> description_details = getIntent().getStringArrayListExtra("description");
        final ArrayList<String> link_details = getIntent().getStringArrayListExtra("link");
        ArrayList<String> pubDate_details = getIntent().getStringArrayListExtra("pubDate");
        final int position = getIntent().getIntExtra("pos", 0);

        text_title = findViewById(R.id.text_title);
        text_title.setText(title_details.get(position));

        text_description = findViewById(R.id.text_description);
        text_description.setText(description_details.get(position));

        text_date = findViewById(R.id.text_date);
        text_date.setText(pubDate_details.get(position));

        text_link = findViewById(R.id.text_link);
        text_link.setText(link_details.get(position));

        edit_notes = findViewById(R.id.edit_notes);


        //send you to bbc website
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(link_details.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //insert into database on button click
        fav_btn = findViewById(R.id.favorite_btn);
        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = text_title.getText().toString();
                String description = text_description.getText().toString();
                String pubDate = text_date.getText().toString();
                String link = text_link.getText().toString();
                String notes = edit_notes.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(notes, notes);

                Boolean checkInsert = db.insertIntoDatabase(title, description, pubDate, link, notes);
                if (checkInsert == true) {
                    Toast.makeText(LinkActivity.this, getString(R.string.toast), Toast.LENGTH_LONG).show();
                    fav_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_full));
                } else {
                    Toast.makeText(LinkActivity.this, getString(R.string.else_toast), Toast.LENGTH_SHORT).show();;
                }

            }
        });

    }
}
