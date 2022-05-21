package com.example.android.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {

    //Constructor
    public NewsItemAdapter(@NonNull Context context, @NonNull List<NewsItem> newsItemList) {
        super(context, 0, newsItemList);
    }

    //Get View method
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        //Getting the current newsItem
        NewsItem newsItem = getItem(position);

        //For the section text
        //Get the section text from list item
        TextView section_name = listItemView.findViewById(R.id.section_name_text);
        //set section name
        section_name.setText(newsItem.getSection_name());

        //For the type text
        //Get the type text from list item
        TextView type_of_section = listItemView.findViewById(R.id.type_text);
        //set type name
        type_of_section.setText(newsItem.getType_of_section());

        //For the web title text
        //Get the web title text from list item
        TextView web_title_text = listItemView.findViewById(R.id.webTitle_text);
        //set the web title name
        web_title_text.setText(newsItem.getWeb_title());

        //For the pillar name text
        //Get the pillar name text from list item
        TextView pillar_name_text = listItemView.findViewById(R.id.pillar_name_text);
        //set the web title name
        pillar_name_text.setText(newsItem.getPillar_name());

        //For the date text
        TextView publication_date_text = listItemView.findViewById(R.id.web_publication_date_text);
        publication_date_text.setText(newsItem.getPublication_date().substring(0,10));

        //For the onClickListener to go to website for more data!
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = newsItem.getWebUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                getContext().startActivity(intent);
            }
        });
        return listItemView;
    }

}
