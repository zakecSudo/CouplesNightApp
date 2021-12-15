package com.ris.couplesnight.classes;

import androidx.annotation.NonNull;

public class Movie {

    private String id;
    private String title;
    private String year;
    private String runtimeStr;
    private String plot;

    public Movie() {
    }

    public Movie(String title) {
        this.title = title;
    }

    public Movie(String id, String title, String year, String runtimeStr, String plot) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.runtimeStr = runtimeStr;
        this.plot = plot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        year = year;
    }

    public String getRuntimeStr() {
        return runtimeStr;
    }

    public void setRuntimeStr(String runtimeStr) {
        this.runtimeStr = runtimeStr;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @NonNull
    @Override
    public String toString() {
        return "Title: " + this.title + "\n" + "Description: " + this.plot;
    }
}
