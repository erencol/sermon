package com.erencol.sermon.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sermon implements Serializable {
    @SerializedName("id") public int id;
    @SerializedName("title") public String title;
    @SerializedName("shortText") public String shortText;
    @SerializedName("image") public String imageUrl;
    @SerializedName("date") public String date;
    @SerializedName("text") public String text;
    @SerializedName("new") public Boolean isNew = false;

    public Sermon(int id, String title, String shortText, String imageUrl, String date, String text) {
        this.id = id;
        this.title = title;
        this.shortText = shortText;
        this.imageUrl = imageUrl;
        this.date = date;
        this.text = text;
    }

    public Sermon() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }
}
