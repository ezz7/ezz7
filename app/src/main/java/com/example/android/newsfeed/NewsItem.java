package com.example.android.newsfeed;

public class NewsItem {
    //Declaring variable and making them private
    private String section_name;
    private String type_of_section;
    private String web_title;
    private String pillar_name;
    private String publication_date;
    private String webUrl;

    public NewsItem(String section_name, String type_of_section, String web_title, String pillar_name, String publication_date, String webUrl) {
        this.section_name = section_name;
        this.type_of_section = type_of_section;
        this.web_title = web_title;
        this.pillar_name = pillar_name;
        this.publication_date = publication_date;
        this.webUrl = webUrl;
    }

    //Getters
    public String getSection_name(){
        return section_name;
    }
    public String getType_of_section(){
        return type_of_section;
    }
    public String getWeb_title(){
        return web_title;
    }
    public String getPillar_name(){
        return pillar_name;
    }
    public String getPublication_date(){
        return publication_date;
    }
    public String getWebUrl(){
        return webUrl;
    }
}
