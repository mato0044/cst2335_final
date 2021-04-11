package com.example.bbcnews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ListView rss_view;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;

    ArrayList<String> title;
    ArrayList<String> link;
    ArrayList<String> description;
    ArrayList<String> pubDate;


    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("BBC News ver 1.0");
        rss_view =(ListView) view.findViewById(R.id.rss_view);
        title =new ArrayList<String>();
        link =new ArrayList<String>();
        description =new ArrayList<String>();
        pubDate =new ArrayList<String>();

        //send data to link activity for more information
        rss_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
                Intent intent = new Intent(getContext(), LinkActivity.class);

                intent.putExtra("pos", position);
                intent.putStringArrayListExtra("title", (ArrayList<String>) title);
                intent.putStringArrayListExtra("description", (ArrayList<String>) description);
                intent.putStringArrayListExtra("link", (ArrayList<String>) link);
                intent.putStringArrayListExtra("pubDate", (ArrayList<String>) pubDate);
                startActivity((intent));
            }
        });
        new ProcessInBackground().execute();
        return view;
    }
    private InputStream getInputStream(URL url) throws IOException {
        return url.openConnection().getInputStream();
    }

    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception> {
        Exception exception = null;
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        ProgressBar progressBar = new ProgressBar(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Loading articles");
            progressBar.setVisibility(View.VISIBLE);
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {
            try
            {
                URL url = new URL("https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();
                // get the xml
                xpp.setInput(getInputStream(url), "UTF_8");
                boolean items = false;
                // sorts through tags
                int event = xpp.getEventType();

                while (event != XmlPullParser.END_DOCUMENT) {

                    if (event == XmlPullParser.START_TAG) {
                        //desired data is in tag item
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            items = true;
                        }
                        //else if statements to acquire data
                        //get title
                        else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (items) {
                                title.add(xpp.nextText());
                            }
                        }
                        //get the link
                        else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (items) {
                                description.add(xpp.nextText());
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (items) {
                                link.add(xpp.nextText());
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            if (items) {
                                pubDate.add(xpp.nextText());
                            }
                        }

                    }
                    //move on to the next item tag
                    else if (event == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        items = false;
                    }
                    //iterate through the list
                    event = xpp.next();
                }
            } catch (MalformedURLException e) {
                exception = e;
            } catch (XmlPullParserException e) {
                exception = e;
            } catch (IOException e) {
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, title);
            rss_view.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            progressDialog.dismiss();
        }
    }
}
