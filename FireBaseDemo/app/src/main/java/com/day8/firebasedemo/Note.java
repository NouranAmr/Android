package com.day8.firebasedemo;

public class Note {
    private String title;
    private String body;
    public Note()
    {

    }
    @Override
    public String toString() {
        return title;
    }

    public Note(String noteTitle, String noteBody)
    {
        title=noteTitle;
        body=noteBody;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
