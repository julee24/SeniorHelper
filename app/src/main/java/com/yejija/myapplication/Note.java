package com.yejija.myapplication;

public class Note {
    int _id;
    String weather;
    String locationX;
    String locationY;
    String contents;
    String mood;
    String picture;
    String createDateStr;

    public Note(int _id, String weather, String locationX, String locationY, String contents, String mood, String picture, String createDateStr) {
        this._id = _id;
        this.weather = weather;
        this.locationX = locationX;
        this.locationY = locationY;
        this.contents = contents;
        this.mood = mood;
        this.picture = picture;
        this.createDateStr = createDateStr;
    }

    public String getWeather() {
        return weather;
    }

    public String getContents() {
        return contents;
    }

    public String getMood() {
        return mood;
    }

    public String getPicture() {
        return picture;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

}
